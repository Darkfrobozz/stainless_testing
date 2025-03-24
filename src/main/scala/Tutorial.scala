import stainless.annotation.* 
import stainless.lang.*

object Tutorial {

  // You can start writing your code here
  def fac(n: Int) : Int = {
    if (n == 0) 1 else fac(n-1) * n
  }

}