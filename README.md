# cljs-rete4f

ClojureScript RETE for frames engine.

## Overview

General functionallity described in [rete4frames] (https://github.com/rururu/rete4frames) project. To get this functionality in ClojureScript include this engine into your ClojureScript program.

## Setup

Include in you projects.clj file this dependency:
```clojure
:dependencies [..
               [cljs-rete4f "0.1.0-SNAPSHOT"]
               ..]
```
Create in your src folder 4 files:

1. templates.cljs
2. rules.cljs
3. facts.cljs
4. f.cljs - functions.

Fill them as appropriate. Function names in rules.clj file must be fully-qualified. Call "run-with" function in your application program with corresponding afguments.
For details see sample projects in corresponding folder.

## License

Copyright © 2016 Ruslan Sorokin.

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
