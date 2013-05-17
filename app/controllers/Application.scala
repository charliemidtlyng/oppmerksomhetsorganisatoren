package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data.Forms._
import play.api.data._
import libs.json.Json

object Application extends Controller {


  def index = Action {
    Ok(views.html.index());
  }

  def personer = Action {
    Ok(Json.toJson(Person.all()))
  }

  val personForm: Form[Person] = Form(
    mapping(
      "id" -> optional(longNumber),
      "navn" -> text,
      "fodselsdato" -> jodaDate("yyyy-MM-dd"),
      "adresse" -> mapping(
        "adresseId" -> optional(longNumber),
        "adressenavn" -> optional(text),
        "postnummer" -> optional(text),
        "poststed" -> optional(text)
      )
        ((adresseId: Option[Long], adressenavn:Option[String], postnummer: Option[String], poststed: Option[String]) => {
          println("----------" + adresseId)
          if(adresseId.isEmpty && (adressenavn.isEmpty || postnummer.isEmpty || poststed.isEmpty)){
            BadRequest(Json.toJson("Mangler adresseinfo"))
          }
          if(adresseId.isEmpty) {
            val a:Option[Long] = Adresse.opprett(Adresse(adresseId, adressenavn.get, postnummer.get, poststed.get))
            Adresse.finn(a.get)
          } else {
            Adresse.finn(adresseId.get)
          }
        })
        ((adresse: Adresse) => Some(adresse.id, Option(adresse.adressenavn), Option(adresse.postnummer), Option(adresse.poststed))),
      "info" -> text(0)

    )(Person.apply)(Person.unapply)
  )

  def nyPerson = Action(parse.tolerantJson) {
    implicit request =>
      personForm.bindFromRequest.fold(
        errors => BadRequest(Json.toJson(errors.errorsAsJson)),
        person => {
          println(person)
          val p = Person.opprett(person)
          Ok(Json.toJson(p))
        }
      )
  }


  def slettPerson(id: Long) = Action {
    Person.slett(id);
    Ok(Json.toJson(id))
  }

  // Adresse
  val adresseForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "adressenavn" -> text,
      "postnummer" -> text(0, 4),
      "poststed" -> text

    )(Adresse.apply)(Adresse.unapply)
  )

  def adresser = Action {
    Ok(Json.toJson(Adresse.all()))
  }

  def nyAdresse() = Action {
    implicit request =>
      println(adresseForm.hasErrors)

      adresseForm.bindFromRequest.fold(
        errors => BadRequest(Json.toJson(errors.errorsAsJson)),
        adresse => {
          val a = Adresse.opprett(adresse)
          Ok(Json.toJson(a))
        }
      )
  }

  def slettAdresse(id: Long) = Action {
    Adresse.slett(id)
    Ok(Json.toJson(id))
  }

}