package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import libs.json.Json
import models.enums.{Rolle, Hendelsestype, Involverttype}
import play.api.data.format.Formats.doubleFormat

import play.api.data.Forms._
import AnormExtension._

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 * @since 2.47
 */
object OppmerksomhetController extends Controller {

  val oppmerksomhetForm: Form[Oppmerksomhet] = Form(
    mapping(
      "id" -> optional(longNumber),
      "til" -> mapping(
        "id" -> optional(longNumber))
        ((tilId) => {
          val a: Involvert = new Involvert() {
            val id: Option[Long] = tilId
          }
          Option(a)
        }
        )
        ((t: Option[Involvert]) => Option(t.get.id)),
      "tilType" -> optional(enum(Involverttype)),
      "fra" -> mapping(
        "id" -> optional(longNumber))
        ((fraId) => {
          val a: Involvert = new Involvert() {
            val id: Option[Long] = fraId
          }
          Option(a)
        }
        )
        ((t: Option[Involvert]) => Option(t.get.id)),
      "fraType" -> optional(enum(Involverttype)),
      "url" -> optional(text),
      "info" -> optional(text),
      "verdi" -> optional(of(doubleFormat)),
      "tid" -> jodaDate("yyyy-MM-dd"),
      "hendelsestype" -> enum(Hendelsestype),
      "levert" -> boolean,
      "rolle" -> enum(Rolle)

    )(Oppmerksomhet.apply)(Oppmerksomhet.unapply)
  )

  def nyOppmerksomhet = Action(parse.tolerantJson) {
    implicit request =>
      oppmerksomhetForm.bindFromRequest.fold(
        errors => BadRequest(Json.toJson(errors.errorsAsJson)),
        oppmerksomhet => {
          val p = Oppmerksomhet.opprett(oppmerksomhet)
          Ok(Json.toJson(p))
        }
      )
  }

  def oppmerksomheter = Action {
    Ok(Json.toJson(Oppmerksomhet.all()))
  }

  def oppmerksomhet(id: Long) = Action {
    Ok(Json.toJson(Oppmerksomhet.finn(id)))
  }
}
