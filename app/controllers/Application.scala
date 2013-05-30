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

}