---
type: post
title: "My gentle introduction to hard-core open-source contribution"
description: "I had made my first major open-source contribution to a popular Clojure library. Learn from my experience and start contributing!"
author: Eccentric J
author-email: jayzawrotny@gmail.com
location: New York, USA
date-created: 2019-06-13T22:13:00-4:00
date-modified: 2019-06-26T22:52:00-4:00
date-published: 2019-06-23T22:16:42-4:00
in-language: en
uuid: 70384D51-ECA5-422E-B682-A7D2F4D93835
tags:
 - experience
 - clojure
---
Back when I started learning Clojure, I never believed I would be able to contribute to a substantial library. After only a year and a half, I was able to refactor some of <a href="https://nrepl.org/">nREPL</a>’s command-line logic! Not only was the process enjoyable, but I also learned a lot from experienced maintainers. This article reflects on my first contribution to a significant Clojure library. I hope others feel encouraged to contribute to major open-source projects as well.


## The Problem

I wanted to reuse nREPL’s command-line logic to provide a more engaging <abbr title="Read-Eval-Print-Loop" class="initialism">REPL</abbr> experience for new users.  Unfortunately, the command-line logic I required was [one lengthy function](https://github.com/nrepl/nrepl/blob/054be705c057d352c4eddfceb68fc0c0c17e25aa/src/clojure/nrepl/cmdline.clj#L242) with many side-effects.  While I wouldn’t call it bad code, I would have to copy and change many parts of it.  If nREPL’s cmdline interface changes between versions, it may break my library.


## Why was Clojure more approachable?

While I have more experience in other languages, I never found myself able to contribute much.  What is it about Clojure that made it feel more natural for me to jump in and start contributing?

### 1. Clojure makes the intent of functions clear

Clojure encourages developers to build complex systems made up of smaller, pure functions. Pure functions only take input and use it to calculate the output.  They do not affect the outside world and have a clear intent.  In contrast, functions that have many side-effects tend to blur intent.  A Clojure function with unclear intent is an excellent place to start a refactor.  For instance, the original [nREPL cmdline code](https://github.com/nrepl/nrepl/blob/054be705c057d352c4eddfceb68fc0c0c17e25aa/src/clojure/nrepl/cmdline.clj#L242) has a lot going on.  It dispatches sub-commands, starts servers, connects to servers, and starts the REPL interface.  This meaty function became my starting point.  Refactoring that function improves the library and also solves my reusability problem as well.

### 2. Functional Programming Principles Keep People's Efforts in Sync

One of my favorite aspects of functional programming is how principled it is. Principles like immutability, first-class functions, purity, and composability help keep programs simple. While developers can use them in most languages, it relies on team discipline to stick to them. Instead, functional programming languages, like Clojure, bake functional principles into their core. When Clojure code defies those principles, it sticks out like a sore thumb.

Functional principles have full buy-in from the community in languages like Clojure as well.  The mindshare provides an intuition for how to design programs.  Small, pure functions form pipelines to build up more meaningful data over time.  Side-effects nestle at the edges of programs, such as the beginning or end of pipelines.  Having that common intuition made learning how nREPL works quite simple.  That ease of learning felt quite unlike my experience with Node, where you find gigantic functions with many side effects.  A single function may use mutable state, nested callbacks, and complex decision trees.  Both Node source code and [community NPM modules](https://github.com/auth0/node-jsonwebtoken/blob/master/verify.js#L18) have chunky, confusing functions.  Where would you even begin to refactor that?  It often feels like there are no principles but a wild-west of do-whatever-makes-sense or seems-to-work-at-the-time.  When a program is made up of many small functions with clear intent, it is much easier to refactor.  Programs with lengthier functions that cause many side-effects often require massive rewrites.


The principled design also made it easy to make a public case for my refactor.  I was able to express how such a meaty function was not reusable, testable, and inhibited my side project.  Without shared principles, proving the value in a refactor can be more difficult.  I only had to show how that code works against the principles followed in the rest of the project.  Without the principles, I would be stuck arguing that my style is somehow better than the actual working code :dizzy_face:

### 3. Isolated Blocks

Since Clojure code leverages pure functions, my code still works provided my functions take similar input and return the expected output.  The automated tests also further proved that I did not break any existing behavior as well.


## Getting Started

Before starting this project, I felt a bit of doubt and uncertainty.  How do I know if I’m solving a real problem or if I am only nit-picking aesthetics?  I took to the Clojurians Slack and asked if there was merit in my issues.  I wanted to be sure I’m taking up maintainers time for something valuable.
The Clojurian sages in Slack gave me some great advice:

1. Create an issue first, so maintainers verify the problem and your proposed solution.
2. Focus the issue on the problem the code creates instead of attacking how the code looks.


## The Process

1. Created an [issue on GitHub](https://github.com/nrepl/nrepl/issues/108) to identify the problem and propose a solution
2. After discussion, started drafting the implementation
3. Created a [GitHub pull-request](https://github.com/nrepl/nrepl/pull/116) with my refactor draft
4. Discussed the pull-request with project maintainers
5. Refined the implementation based on the pull-request feedback
6. Wrote tests for the new functions I created
7. Added a feature suggested by Bozhidar: <br /> A `repl-fn` option to swap out the interactive REPL function
8. Added tests to confirm the `repl-fn` option behavior
9. Pull request was approved, then merged into the master branch


## What did I learn?

<dl class="dl--title-size_3 dl--title-mb_1 ml-3">
  <dt>Be diplomatic</dt>
  <dd><p>Raise issues around a problem instead of criticizing others' code.</p></dd>

  <dt>Pick battles that are worth the time</dt>
  <dd>
    <p>
      Pick battles that are worth the time.  Everyone is busy, pick meaningful battles about architecture and design.  Not details like if side effect functions should end with a <code>!</code> or not.  Try to keep your contributions aligned with the maintainer's goals.
    </p>
  </dd>

  <dt>You can learn a lot by reading the source code</dt>
  <dd><p>From reading nREPL's source, I learned how to develop more robust and sophisticated <abbr title="Command Line Interface" class="initialism">CLI</abbr>s in Clojure.</p></dd>

  <dt>Squash commits before merging</dt>
  <dd>
    <p>
      Using git rebase to squash commits before merging keeps git history meaningful.  Now the whole refactor I did lives in one commit!  If I introduced a bug, a smart tool like <code>git bisect</code> could pinpoint my commit, making it safer to revert.
    </p>
  </dd>
</dl>


## What could I have done better?

Reflecting on the experience, a few areas for improvement come to mind:

1. Maintainers' time is valuable. I had asked a few questions that I could have answered with a little more research. I should try to research answers on my own first before publicly asking questions.
2. Don’t expect step-by-step guidance. Sometimes I should draft a solution instead of waiting for step-by-step instructions or pre-approval.
3. Be more mindful when explaining my design decisions. In some cases, I over-explained my rationale when it was already known and may have come off defensive. Too much defensiveness wears people down.
4. Pace myself so that I’m not overworking one day then recovering the week after. On the days I limited my work on the nREPL to about 20 minutes, I had no problem picking it up the next day.


## Final Thoughts

Overall working with the nREPL maintainers was a great experience!  I enjoyed the project and learning from code reviews.  I hope my contributions are valuable, and my refactor helps others create beautiful REPL experiences.  Given how welcoming and informative the Clojure community is, I look forward to contributing to more projects.

I want to give a major shout out to [Bozhidar Batsov](https://metaredux.com/) (aka bbatsov).  He was responsive, open, and informative in guiding my inexperienced hands throughout the refactor. His stewardship is an excellent example of how to lead an open-source project.

