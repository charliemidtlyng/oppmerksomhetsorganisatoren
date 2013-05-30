package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {
  
  "Application" should {
    
    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone        
      }
    }
    
    "render the index page" in {
      running(FakeApplication()) {
        val home = route(FakeRequest(GET, "/")).get
        
        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
        contentAsString(home) must contain ("Oppmerksomhetsorganisatoren")
      }
    }

    "fetch all persons" in {
      running(FakeApplication()) {
        val personer = route(FakeRequest(GET, "/personer")).get

        status(personer) must equalTo(OK)
        contentType(personer) must beSome.which(_ == "application/json")
        contentAsString(personer) must contain ("[")
        contentAsString(personer) must contain ("]")
      }
    }
  }
}