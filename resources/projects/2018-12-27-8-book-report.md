---
type: project
title: Book Report
description: A Clojure macro to turn source files into interactive notebooks.
author-email: jayzawrotny@gmail.com
location: New York, USA
date-created: 2019-01-27T12:12:00-5:00
date-modified: 2019-01-27T12:12:00-5:00
date-published: 2018-12-27T22:10:12-5:00
in-language: en
uuid: f9836f0c-0b5f-4657-92c1-8eb370dafcf8
bg: "#f0eee6"
github: eccentric-j/book-report
website:
languages:
 - clojure
---
# <span class="project__goal">Goal</span>

When learning Clojure from books there is no great solution for organizing notes, exercises, and examples. Online notebooks only provide a subset of Clojure functionality and using comments mean evaluation and documentation are separated providing difficulty in revisiting notes later on. For instance I ended up sinking an hour on the first few examples from a chapter only to find out the notebook service I was using doesn&rsquo;t fully support full Clojure functionality. Not only is debugging made more difficult with add an extra layer of frustration but now there is this extra context you need to be aware of when trying to follow along with examples.

I wanted a full Clojure experience that would allow me to organize what I&rsquo;ve read and wrote into bite-sized lessons. It needed to let me revisit my notes and not lose any context such as the code that was written, what it does, and what I want to learn from it. It also needs to feel natural to use so retyping code as a string to show along side evaluation results would not cut it. Book Report allows you to send notes and code to a lesson macro and it gets printed with the notes, the source code, and the evaluation results to provide the whole story.

## <span class="project__process">Process</span>
1. Learn how to work with macros from Clojure for the Brave and True book.
2. Researched if a similar macro existed already in Clojure, didn&rsquo;t see anything similar.
3. Wrote a quick and dirty version of the macro, pushed it up, asked for feedback in the Clojure slack community.
4. Community feedback was positive. Cleaned up code, added new features.
5. Wrote unit tests where appropriate.
6. Designed a logo, for fun, to give the library a more polished look.
7. Announced the library in both Clojure Reddit and Clojure Slack.

## <span class="project__discoveries">Discoveries</span>
- Got a strong first taste of the power of Clojure macros. They&rsquo;re _quite_ powerful!
- I can work from books a bit faster now :smile:
- Learned that asking simple, more general Clojure questions in the Slack received more answers with better discussion than asking very tool-specific questions. For instance asking "What is the best way to concat two lists together?" provided way better answers than "How do I combine two vectors of reitit route collections?".
