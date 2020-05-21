package alexa

import akka.actor.{Actor, ActorLogging}

import scala.collection.mutable.ArrayBuffer

class ToDoListActor extends Actor with ActorLogging {

    val toDoList = new ArrayBuffer[String]()
    val brain = context.actorSelection("/user/Brain")

    val regexesWeRespondTo = List(
        "^todo.*"
    )
    
    val letUserKnowPhrasesWeRespondTo = List(
            "todo add <foo>",
            "todo list",
            "todo view",
            "todo del <foo>",
            "todo rem <foo>"
    )

    def receive = {
        
        case WillYouHandlePhrase(s) =>
            val weWillHandle = Utils.matchesARegex(regexesWeRespondTo, s)
            sender ! weWillHandle
            if (weWillHandle) {
                log.info(s"ToDoListActor will handle '$s'")
                handleThePhrase(s)
            }

        case WhatPhrasesCanYouHandle =>
            sender ! letUserKnowPhrasesWeRespondTo

        case unknown =>
            log.info("ToDoListActor got an unknown message: " + unknown)

    }

    private def handleThePhrase(s: String): Unit = {
        val words = s.split(" ")
        // make sure the list has at least two words
        if (words.length < 2) {
            sendMessageToUser(s"ToDoList could not understand text: '$s'")
            return
        }
        if (words(0) != "todo") {
            sendMessageToUser(s"ToDoList could not understand text: '$s'")
            return
        }
        val secondWord = words(1)
        secondWord match {
            case "add" =>
                handleAdd(words.tail.tail.mkString(" "))
            case "list" | "view" =>
                handleList
            case "del" | "rm" =>
                handleDelete(words.tail.tail.mkString(" "))
            case unknown =>
        }
    }
    
    private def handleAdd(s: String) {
        toDoList.append(s)
        handleList
    }
    
    private def handleList() {
        brain ! SpeakList(toDoList)
    }

    private def handleDelete(s: String) {
        val idx = toDoList.indexOf(s)
        if (idx >= 0) toDoList.remove(idx)
        handleList
    }

    private def sendMessageToUser(s: String) {
        brain ! SpeakText(s)
    }

}













