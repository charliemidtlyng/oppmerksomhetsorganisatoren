package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data.Forms._
import play.api.data._
import libs.json.Json
/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 * @since 2.47
 */
object AdresseController extends Controller {
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
