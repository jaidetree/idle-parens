---
type: project
title: ClojureScript TUI Template
description: A ClojureScript terminal-user-interface template built on web technologies you already know.
author-email: jayzawrotny@gmail.com
location: New York, USA
date-created: 2019-02-24T11:48:00-5:00
date-modified: 2019-02-24T18:30:00-5:00
date-published: 2019-02-24T18:30:00-5:00
in-language: en
uuid: fcc2a75d-7d23-4f00-9cb4-9a87b112895b
bg: "#ff00ff"
fg: "#2f2b33"
github: eccentric-j/cljs-tui-template
website:
languages:
 - clojure
 - clojurescript
---
# <span class="project__goal">Goal</span>

I always wanted to write rich terminal-user-interface applications to better automate mundane tasks and integrate with my common tools. Discovering https://github.com/denisdoro/floki blew me away with how powerful and familiar it was to write applications like that using blessed js, a curses implementation in node. Inspired by its stack I knew I had to make a template for other developers to be able to get started quickly.

This project is one of my first ClojureScript projects, I felt intimidated by the tooling required and put off diving in. However, while working on this project I found it may be valuable to try each major build tool: lein-figwheel, figwheel-main, and shadow-cljs to learn how to use them. Now I'm a lot more comfortable working in ClojureScript and look forward to doing more with it.

The reason I chose ClojureScript for this project is that you can distribute your transpiled apps with NPM and the startup time is significantly faster than Java based Clojure.

## <span class="project__process">Process</span>
1. Studied Floki's stack
2. Designed base architecture suitable for most use-cases
3. Created a prototype UI
4. Researched figwheel-main
5. Added +figwheel-main option
6. Researched shadow-cljs
7. Added +shadow-cljs option
8. Made shadow-cljs the default
9. Designed a small demo app to show what it can do

## <span class="project__discoveries">Discoveries</span>
- Dived much deeper into ClojureScript
- Learned how to work with Reagent's atoms and life-cycle functions
- Learned lein-figwheel
- Learned figwheel-main
- Learned shadow-cljs
- Discovered how amazing shadow-cljs is for ClojureScript apps
