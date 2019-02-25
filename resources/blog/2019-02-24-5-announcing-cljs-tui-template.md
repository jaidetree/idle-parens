---
type: post
title: "Announcing my Leiningen ClojureScript Terminal-User-Interface Application Template :tada:"
description: "Create ClojureScript TUI applications using web technologies you already know"
author: Eccentric J
author-email: jayzawrotny@gmail.com
location: New York, USA
date-created: 2019-02-23T01:09:00-5:00
date-modified: 2019-02-24T23:00:00-5:00
date-published: 2019-02-24T18:30:00-5:00
in-language: en
uuid: 406fbf95-11d3-476b-8435-0c3c07fbff66
tags:
 - announcement
 - clojure
 - clojurescript
---

## The Beginning

I've always wanted to make rich, expressive terminal-user-interface applications such as htop, vim, or tmux but felt deterred by the absurd amount of state that needs to be maintained even for the simplest of visual effects.

Earlier this year someone posted a link to https://github.com/denisdoro/floki and it totally blew my mind! It's a JSON browser written in ClojureScript using Reframe, Reagent, React, and Blessed with graphical windows and keyboard navigation. I had no idea [blessed](https://github.com/chjj/blessed) even existed! I knew I had to make a template out of Denis' stack so others like myself can quickly make our own awesome terminal user interface applications.

The project can be found at:

https://github.com/eccentric-j/cljs-tui-template

## Why ClojureScript?

This is the first question most people will likely ask upon discovering this project. At this time however, I no longer believe in "best-tool-for-the-job". It's such a personal, relative, and temporary measure so I would rather not incite a language war trying to suggest how ClojureScript could be objectively better than any other language. Instead it's currently the language I enjoy the most. Its ethos gels with mine and I enjoy the development process it overs and I hope this project inspires curiosity to give it a shot. That said I will share my reasons for enjoying it so much.

## Functional Programming
Functional programming provides strong principles that guide all implementation decisions. In most cases I've found it helps me make more reusable, simpler, and concise units of code I can compose together to make more complex systems. Because of the simpler pieces I find it's much easier to test, write, document, maintain, and debug.

## Clojure Syntax
Clojure's syntax is very different than traditional c-family languages since it's in the LISP family. It was very jarring to me when I first encountered it, but I love it now. I will one day write an entire article about Clojure syntax but what in short it's very consistent. There's very few pieces to it and no operator precedence to memorize, forms have a strong visual structure to them I find easy to scan and can be edited quickly as structured data with simple keystrokes in well-suited editors.

## Strong, Dynamic Typing
I understand the value strong, static typing brings, but I personally prefer focusing my attention on getting an app to work versus writing specifications to prove that my program could work. ClojureScript makes JS a bit closer to a strongly typed, dynamic language which I feel offers a lot of flexibility but also more certainty than the loose, dynamic typing found in vanilla JS.

## Live editing
Develop faster using the ClojureScript REPL to send code straight from your editor to the app while preserving the runtime app state. It would be great to see a stronger REPL experience make its way into JS.

## Low boiling point
Since learning Clojure I spend far less time reading docs, and more time glancing at source code to solve my problems to get cooking faster. That's not to say good documentation isn't valuable, in Clojure I don't often require much of it to make progress compared to my experience in other languages.

## Faster Startup with ClojureScript
Compared to Clojure, ClojureScript startup time is significantly faster running on node. This is important because CLIs or TUIs should fire up quickly.

# Getting Started

Poking around Floki's source demonstrated how the required libs should fit together and how to develop the template with lein-figwheel and lein-cljsbuild. After some trial and error plus many questions in the Clojurians Slack I forged a simple, working prototype.

# Lein Figwheel

Just as soon as I moved towards the peak of this mountain I discovered an even larger peak: The creator of lein-figwheel created a successor project, figwheel-main, for building the JS. In addition to better documentation, it provides a more robust configuration strategy, better build performance, a rigid spec to test your configurations, and a project no longer needed the lein-cljsbuild companion plugin. Since I am both naive and foolish, I ambitiously embarked on supporting a figwheel-main option.

# Figwheel Main

Supporting figwheel-main turned into a more arduous task than expected. I'm having trouble pinpointing exactly why. It could be that for lein-figwheel I had a working example from floki to go by, or it could largely be the way the docs are organized. While figwheel-main has a lot more documentation than lein-figwheel it conflates practical guides with reference documentation. One of the initial struggles was having to figure out that your `figwheel-main.edn` file only configures figwheel tooling behavior while your `<build-id>.cljs.edn` files are essentially compile options for a specific build. You can override figwheel-main config from a cljs config by adding a meta hash-map to your cljs config map.

```clojure
^{:watch-dirs ["src" "env/dev"]}
{:main my-test-project.app
 :asset-path           "target/js/compiled/dev"
 :output-to            "target/js/compiled/my-test-project.js"
 :output-dir           "target/js/compiled/dev"
 :target               :nodejs
 :optimizations        :none
 :source-map           true
 :source-map-timestamp true}
```

For instance the `:watch-dirs` map overrides `figwheel-main.edn` options. It makes some sense now but I think a `:figwheel` key in the cljs.edn files would have been my preference.

Another area where I think figwheel-docs lack is in node development, especially working with npm deps. The floki project uses the :npm-deps and :install-deps config options with lein-figwheel to manage the npm modules automatically. The advantage of this approach is that it automatically handles updating the package.json, installing deps, and allows ns imports, shown below.

```clojure
(ns my-test-project
  (:require ["react" :as react]))
```

While this syntax is preferred, I decided it would probably be better to make the template use package.json for deps so devs can make the most out of what npm offers. However, it turns out you cannot use the ns syntax from manual package.json deps. It only works using the ClojureScript :npm-deps and :install-deps build options. After some slack questions and testing against figwheel's detailed config spec I found that js interop is the recommended way to use npm modules anyway.

```clojure
(def react (js/require "react"))
```

Not quite as nice as the ns syntax but became essential for getting the test runner to work. I just couldn't get tests to run with ns import syntax even with the :npm-deps and :install-deps build options. I created an [issue in figwheel-main](https://github.com/bhauman/figwheel-main/issues/148) to learn more and discuss solutions.

After stumbling my way to a working config with figwheel-main I once again had a running UI. While researching figwheel-main posts I noticed someone usually full-heartedly recommends shadow-cljs. Seeing so many comments like that convinced me it would be worth supporting as well. While it is yet another build tool to learn, this project has been a great excuse to give each cljs build tool a shot. So with a deep sigh I set off to reach the next mountain peak of this project.

## Shadow-CLJS

Shadow turned out to be amazing. Where as lein-figwheel was a small foothill and figwheel-main like free-climbing a mountain, shadow-cljs felt like walking along a boardwalk at a partially populated pier with a legal open-beverage policy. It's very well documented and everything really did just work after a minimal amount of reading the docs and a few configuration attempts. It's a very impressive tool and its attention is very well deserved. If you're starting a green ClojureScript project I can't recommend shadow-cljs enough. It even has very solid, official support for nodejs right out of the gate.

The only noticeable caveat is that shadow doesn't really support separate dev and production source paths like Clojure, lein-figwheel, and figwheel-main do. This template shows a debug box during development and captures log statements on the bottom half of the screen. However, devs wont likely want it there on production builds and I don't want them to have to modify code to disable it between builds. I was able to work around this limitation by customizing the entry points between dev and production and the docs suggest the unused version will be factored out, but somehow I don't feel as certain or good about it. I am very glad though that I was able to work around that and still deliver my intended developer experience.

## Choosing a default build tool

After adding support for shadow I had a decision to make: What should the default build tool be? At first I was leaning towards lein-figwheel as that's what floki uses and is likely the most familiar to Clojure devs focusing on a project.clj config. Then I moved to figwheel-main due to its improved documentation and performance. Once I got shadow working however, it was clearly the best choice.

## Why is Shadow CLJS the default build tool?

1. Great documentation. Shadow-cljs' docs are organized into practical approaches for different kind of builds which meant I could mostly just use what was in the nodejs docs and move on. It didn't feel like solving a puzzle like my experience with figwheel-main's docs did.

2. Easier to configure. Everything goes into a single `shadow-cljs.edn` file in your root project folder. Overriding the configuration for specific builds felt a bit more intuitive than the meta-data approach figwheel-main takes.

3. No project.clj. Shadow is its own standalone npm module and doesn't even require leiningen or a project.clj file. You instead place your Clojure dependencies in the `shadow-cljs.edn` file.

4. Automatic nREPL server. Both lein-figwheel and figwheel-main support running a nREPL server for ClojureScript but it's not very intuitive. The process entails starting a regular Clojure nREPL server with `lein repl` and starting a build server then the ClojureScript REPL interface from within the Clojure REPL. Shadow automatically starts a nREPL server when you start building for development which makes everything feel a lot more slick in my opinion. When connecting to the REPL you still have to start the shadow cljs REPL on top of the Clojure REPL but you also don't need any nREPL middleware unlike figwheel-main and lein-figwheel which require the use of piggieback and sidecar.

5. Active support community. The shadow-cljs slack channel is booming with activity and it's creator [Thomas Heller](https://github.com/thheller) is very helpful and responsive. Unfortunately given the complexity of ClojureScript tooling I suspect issues for users of this template will come up. When that happens I want them to be able to get the help they need quickly to unblock them and shadow-cljs is in a healthier place to do that.

6. Great nodejs testing support. Unlike lein-figwheel and figwheel-main shadow-cljs has a very solid testing setup for nodejs scripts and I found it to be very well documented. It took me a few hours to get lein-figwheel and figwheel-main tests to work but it only took a few minutes to get the shadow-cljs tests to run.

7. Fewer layers. While mentioned briefly in previous points there are just fewer layers of tooling required to get shadow-cljs working and productive. In general I feel like the less layers of tools and abstraction the smoother and more maintainable the system. I'm sure there are exceptions but I think users will appreciate not having to configure lein and ClojureScript middleware on top of their package.json and build-tool specific tooling to get started.

## Dynamic Keyboard Listeners

One of the more interesting problems I ran into was figuring out how to support dynamic key listeners. In floki there's only one set of global keyboard events but what would you do if you had different "pages" that responded to different keys?

Using blessed you can set bindings like the following where screen is a blessed Screen instance:

```clojure
(.key screen #js ["q" "ESC"] #(rf/dispatch [:app/quit]))
```

After reading through a bit more of Reagent's docs I found my solution based on using `with-let`:

```clojure
(defn my-view
  [_]
  (with-keys @screen {["escape" "q"] #(rf/dispatch [:app/quit])}
    [:box "Quit me. I dare you."]))
```

This way the handlers are only created when the view is rendered but are destroyed when the view is removed. This means users can make pretty complex UI without having to juggle extra state to determine which set of key listeners to pay attention to. In the example I'm dispatching a reframe event but you can use local reagent atoms as well if you only want to affect local component state. That's how the generated vertical menu view works.

# Final Thoughts

All in all it was a great project to dive into ClojureScript as I had a lot of fun and learned a lot while assembling this project together. What most excites me is what people will build with it once it's out there, I see a lot of possibilities.
