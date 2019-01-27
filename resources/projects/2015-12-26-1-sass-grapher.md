---
type: project
title: Gulp Sass Grapher
description: A gulp plugin to help graph sass imports based on sass-graph.
author: Eccentric J
author-email: jayzawrotny@gmail.com
location: New York, USA
date-created: 2019-01-27T12:12:00-5:00
date-modified: 2019-01-27T12:12:00-5:00
date-published: 2015-12-26T11:15:00-5:00
in-language: en
uuid: c9fda229-3698-4e19-b580-613eba2cf7be
draft: true
bg: "#af5c5c"
fg: "#fff"
github: eccentric-j/gulp-sass-grapher
website:
languages:
 - JS
---
# <i class="fa fa-trophy icon"></i> Goal

When creating the front-end build tooling for VenueBook we needed a smarter build system that would watch for changes to partial SASS files and only compile the root SASS files that imported them.

## <i class="fa fa-list-ol icon"></i> Process
1. Study how node-sass&rsquo;s watch command was implemented
2. Find a library that could parse a sass file and return imports.
3. Write software to keep a tree map of the relationships between all SASS files in our project.
4. Leverage gulp watch stream to look up files in the tree to find their root parent files.

## <i class="fa fa-lightbulb icon"></i> Discoveries
- One of my first introductions to streams which is what lead me to functional programming.
- Learned how to graph dependencies for quick lookups.
- Gained experience writing complex front-end build pipelines from scratch.
