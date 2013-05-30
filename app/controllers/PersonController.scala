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
object PersonController extends Controller {

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
      "info" -> optional(text)

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

  def personer = Action {
    Ok(Json.toJson(Person.all()))
  }

}
