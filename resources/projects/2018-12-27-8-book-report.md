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
draft: true
bg: "#f0eee6"
github: eccentric-j/book-report
website:
languages:
 - clojure
---
# <span class="project__goal">Goal</span>

When learning Clojure from books there is no best solution to organize the notes, exercises, and examples. The problem is notebook apps only offer a select subset of the Clojure language. For instance I ended up sinking an hour on the first few examples from a chapter on namespaces only to find out the notebook service I was using doesn&rsquo;t support the full Clojure namespace functionality. Not only is figuring that out frustrating but now there is this extra context you need to be aware of when trying to follow along with examples.

I wanted a full Clojure experience that would allow me to organize what I&rsquo;ve read and wrote into bite-sized lessons. It needed to let me revisit my notes and not lose any context such as the code that was written, what it does, and what I want to learn from it. It also needs to feel natural to use so retyping code as a string to show along side evaluation results would not cut it. Without such a tool you may often acquire out-of-date comments of past eval results or you are executing code and getting `nil` unable to remember if that was intentional or if the example was accidentally changed.

## <span class="project__process">Process</span>
1. First I had to read chapter 8 of Clojure for the Brave and True to learn more about macros
2. Followed the examples and solved the exercises.
3. Researched if a similar macro existed already in Clojure, didn&rsquo;t see anything similar.
4. Wrote a quick and dirty version of the macro, pushed it up, asked for feedback in the Clojure slack community.
5. After receiving positive feedback from the community I refined it and supported more features that I thought would be useful in this problem space.
6. Wrote unit tests where appropriate.
7. Designed a logo, for fun, to give the library a more polished look.

## <span class="project__discoveries">Discoveries</span>
- Got a strong first taste of the power of Clojure macros. They&rsquo;re _quite_ powerful!
- I can work from books a bit faster now :smile:
- Learned that asking simpler Clojure questions in the Slack helped me learn more, faster, than asking complex, tool-specific questions. For instance asking "What is the best way to concat two lists together?" provided way better answers than "How do I combine two vectors of reitit route collections?".
