---
type: project
title: File Prompt
description: A node JS command-line-interface (CLI) to quickly select group of commands to pipe to other sources.
author: Eccentric J
author-email: jayzawrotny@gmail.com
location: New York, USA
date-created: 2019-01-27T12:12:00-5:00
date-modified: 2019-01-27T12:12:00-5:00
date-published: 2016-02-23T12:29:00-5:00
in-language: en
uuid: 2872037c-650b-4dd3-a124-c19639bb798d
draft: true
bg: "#ff00b5"
fg: "#fff"
github: eccentric-j/file-prompt
website:
languages:
 - JS
---
# <span class="project__goal">Goal</span>

In an early version of our front-end build system we were using browserify which was great but got slower as we added more build files. Before webpack became its successor I built a CLI tool to select files manually and return a stream of them in vinyl format to be compatible with the gulp library. Heavily inspired by `git add --interactive`.

## <span class="project__process">Process</span>
1. Study behavior of `git add -i`
2. Studied redux, introduced by a coworker, to manage application state
3. Replicate the behavior of `git add -i` in JS
4. Take the selected output and return a stream of vinyl files.

## <span class="project__discoveries">Discoveries</span>
- First redux project
- Learned how to write more complex CLI tools
- Introduced to immutability from working with Redux
- More stream experience which made building even more complex tooling even easier
- Learned how to read input with Node&rsquo;s API.
