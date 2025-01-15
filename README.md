> **Warning**
> As of January 2025 this project has been archived. It's ok to use the code as inspiration or to cannibalize it in any way that is in line with its open source license. The usethesource community has moved on to the [Rascal Language Servers](http://github.com/usethesource/rascal-language-servers) project and the Language Server Protocol, for the kind of functionality that was provided by Impulse before. 

> **Warning**
> The entire Impulse is _deprecated_ starting Februari 1st, 2023. As the Rascal-eclipse project is deprecated,
> which is the only known use of Impulse, we are also deprecating this otherwise valuable project. It is a matter
> of software maintenance capacity of the community. DSL projects which depend on Impulse and Rascal-eclipse will
> be port{ed,able} to VScode support based on the Rascal-language-servers project. 

IMPULSE
=======

IMPULSE is a fork off the IDE Meta Tooling Plaform's core run-time on github which used to be part of an Eclipse IMP incubation project. The code was licensed open-source by IBM and other contributors under the EPL. The IMPULSE fork will not be backward compatible with old versions and focuses on:

* Migrating deprecated API usage to E4 API
* Removing dead code
* Adding missing interaction features
* All this while keeping as much of the investment in the IMPULSE run-time alive: a simple and effective language-oriented abstraction layer over lower-level Eclipse API.

_Currently this fork is in the process of renaming packages and extension point to the UseTheSource and IMPULSE namespaces._

IMPULSE radically simplifies and speeds up the IDE development process in Eclipse, for both language with existing 
front-ends as well as languages generated using compiler and interpreter generation frameworks.

The IMPULSE is essentially a wrapper for Eclipse RCP and some JDT API that let's you focus on your language features 
as opposed to UI features. The core of the run-time is the UniversalEditor class which is a language parametric editor. 
By instantiating the UniversalEditor, using extension points, with information about your own language you can incrementally 
create a basic IDE. You provide this information mainly by implementing a number of simple interfaces.

IMPULSE is described (as IMP) in a 2009 OOPSLA [paper](http://dl.acm.org/citation.cfm?id=1640104)

# Extension points 

The first and only absolutely necessary extension point to bind is "languageDescription". It makes your language known to
Eclipse and triggers a special editor whenever a file with a particular extension is opened:

```
<extension point="io.usethesource.impulse.languageDescription">
      <language description="Hello" extensions=".ext" language="MyLanguage">
      </language>
   </extension>
``` 

The second most important extension point is "parser":

```
 <extension id="myparsecontroller" name="My Language parser" point="io.usethesource.impulse.parser">
      <parser class="org.mylanguage.MyParseController" language="MyLanguage">
      </parser>
   </extension>
```

This will force you to implement [IParseController](https://github.com/usethesource/impulse/blob/master/src/io/usethesource/impulse/parser/IParseController.java), a simple interface which allows you to call your own parser and
return any kind of parse tree, abstract syntax tree or list of tokens. The point of IParseController is to be agnostic in 
what parsing technology you use or what kind of representation you have for the output of the parser. Hence the `parse` method
in IParseController returns an object of type `java.lang.Object`. Downstream this same object will be passed to you again,
for example when you want to add some syntax highlighting.

For now, my time is up describing impulse, but you may have gotten the gist of this now :-) Simply search for "impulse" in 
the editor of plugin.xml to discover more of the extension points of UniversalEditor. It includes features for outline, 
highlighting, menu options, etc.

IMPULSE is not perfect, but it gives you a head start in developing an IDE without too much fuss, and it does not assume
much about the way you want to deal with your own language, except of course that you have a JVM :-)
