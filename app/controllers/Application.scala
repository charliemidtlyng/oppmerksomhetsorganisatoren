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
      "fodselsdato" -> jodaDate("yyyy-MM-dd"),
      "adresse" -> mapping(
        "adresseId" -> longNumber
      )
        ((adresseId: Long) => {
          println("----------" + adresseId)
          Adresse.finn(adresseId)
        })
        ((adresse: Adresse) => Some(adresse.id.getOrElse(0L))),
      "info" -> text

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