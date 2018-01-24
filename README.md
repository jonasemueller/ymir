# Ymir

Opinionated Clojure full-stack starter project. Focuses
on providing a solid basis for building a SPA with
re-frame and a REST backend with yada.

Named after Ymir in Norse mythology, a proto-being who is
the ancestor of important gods and whose dead body was used
to create the world.

These are my opionionated changes on top of Juxt's great starter 
project called edge. It represents the 'edge' of Juxt's current
thinking on the baseline architecture of Clojure projects and 
contains architectural patterns for the rapid construction of
robust and flexible systems.

The alpha in the name does not mean that it is not ready for use,
but that things might still change in a backwards incompatible
way.

## Features

Currently there is a dependency issue with ClojureScript and Java 9.
Therefore it is necessary to set the following environment 
variable when using Java 9.
 
```
export BOOT_JVM_OPTIONS='--add-modules "java.xml.bind"'
```

### A boot-driven Clojure/ClojureScript dev and prod environment

If you don't have Boot installed you can also use the
corresponding executable for your operating system
from the bin folder (boot.sh for Unix, boot.exe for
Windows).

```
cd ymir
boot dev
```

Browse to localhost:3000

To fire up a REPL, open a new session and

```
cd ymir
boot repl
```

Make changes to the Clojure code and reset the system

```
REPL-y 0.3.7, nREPL 0.2.12
Clojure 1.8.0
OpenJDK 64-Bit Server VM 1.8.0_92-b14
        Exit: Control+D or (exit) or (quit)
    Commands: (user/help)
        Docs: (doc function-name-here)
              (find-doc "part-of-name-here")
Find by Name: (find-name "part-of-name-here")
      Source: (source function-name-here)
     Javadoc: (javadoc java-object-or-class-here)
    Examples from clojuredocs.org: [clojuredocs or cdoc]
              (user/clojuredocs name-here)
              (user/clojuredocs "ns-here" "name-here")
user> (reset)
```

### A SASS CSS build

Make changes to the sass files under sass.

### API server

- [A Stuart Sierra component reloaded project](https://github.com/stuartsierra/component)
- Use of `schema.core/defrecord` to validate your system's integrity on every reset
- Run all your tests with `(test-all)`
- bidi & yada for serving web resources and APIs

## ClojureScript REPL

An optional ClojureScript REPL is available. Create a new REPL by connecting to localhost on port 5600. You can do this with boot, lein, CIDER or with another IDE. Once the REPL has started, launch the CLJS repl with the following:

```
user> (cljs-repl)
```

This should connect with your browser and you can then interactively work with cljs.

## Libraries

- aero
- aleph
- bidi
- hiccup
- selmer
- yada

## Editor integration

### Emacs / CIDER.el integration

These instructions are for use with CIDER 0.15 (London). If your Emacs is using
a previous version, you should upgrade now.

From Emacs, use `M-x cider-jack-in`

### Generic CIDER-nrepl integration

See [Boot's instructions on CIDER repl](https://github.com/boot-clj/boot/wiki/Cider-REPL#a-better-way), and run boot with `boot cider dev`

## Deployment

A systemd wrapper script is provided in the top level directory. It takes a parameter which determines the Aero profile to use. In this way you can have multiple systemd units with different profiles on the same machine, even under the same username.

## Copyright & License

The MIT License (MIT)

Copyright © 2016-2017 JUXT LTD.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
