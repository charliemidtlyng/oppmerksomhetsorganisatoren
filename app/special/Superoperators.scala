package special

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 * @since 2.47
 */
object Superoperators {

//  def ??[A](block: => A): Option[A] = ?(block) match {
//    case a: A => Some(a)
//    case _ => None
//  }

  def ?[A](block: => A) =
    try { block } catch {
      case e: NullPointerException if e.getStackTrace()(2).getMethodName == "$qmark" => null
      case e: Throwable => throw e
    }
}
