// package bs.scalaimpatient    // top-of-file notation for chained package clause, for this style we would need the 'import _root_...' statement

package bs
package scalaimpatient
// another 'nested package' style, so this is equivalent to: package bs { package scala impatient { ... }}
// preferred notation of all in this file belongs to this package, which is best practice


// import _root_.bs.BsScopedUtil   // would work, but we must use _root_ here to 

  object Chapter07PackagesScratchTopOfFileNotation extends App {
    BsScopedUtil.demo()   // refers to the ScScopedUtil in Chapter07PackagesScratch.scala
  }
