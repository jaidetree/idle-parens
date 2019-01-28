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

While learning Clojure from books I found myself struggling to find the right place to organize it. After trying different notebook services I found myself frustrated finding out that I followed an example accurately but the notebook app doesn&rquo;t support one specific aspect of Clojure&rsquo;s namespaces that the current chapter I&rsquo;m studying focuses on.

I wanted a full Clojure experience that would allow me to organize what I&rsquo;ve read and wrote into bite-sized lessons I can come back to and not lose any context. Otherwise you may find out-of-date comments of past eval results or you are executing code and getting `nil` back unable to remember if it was the intended result or something wrong with the example.

## <span class="project__process">Process</span>
1. First I had to read chapter 8 of Clojure for the Brave and True to learn more about macros
2. Followed the examples and solved the exercises.
3. Researched if a similar macro existed already in Clojure, didn&rsquo;t see anything similar.
4. Wrote a quick and dirty version of the macro, pushed it up, asked for feedback in the Clojure slack community.
5. After receiving positive feedback from the community I refined it and supported more features that I thought would be useful in this problem space.
6. Wrote unit tests where appropriate.
7. Designed a logo, just for fun to give the tool a more polished look.

## <span class="project__discoveries">Discoveries</span>
- Got a strong first taste of the power of Clojure macros. They&rsquo;re really powerful!
- I can work from books a bit faster now :smile:
- Learned that asking simpler Clojure questions in the Slack helped me learn more than asking complex, tool-specific questions. For instance asking what the best way to concat two lists together I got a lot of great answers.
