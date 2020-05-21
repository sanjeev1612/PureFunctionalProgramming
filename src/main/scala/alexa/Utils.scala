package alexa

object Utils {
  
    def getRandomElement[A](list: Seq[A]): A = {
        val r = new scala.util.Random
        val i = r.nextInt(list.size) 
        list(i)
    }

    def showPrompt: Unit = {
        Thread.sleep(1000)
        print("\nAlexa: ")
    }


    def matchesARegex(regexesWeRespondTo: Seq[String], phrase: String): Boolean = {
        for (regex <- regexesWeRespondTo) {
            if (phrase.matches(regex)) return true;
        }
        false
    }


    def listContainsASubstringOfPhrase(substringsToLookFor: Seq[String], phrase: String): Boolean = {
        for (s <- substringsToLookFor) {
            if (phrase.toLowerCase.contains(s.toLowerCase)) return true;
        }
        false
    }

}