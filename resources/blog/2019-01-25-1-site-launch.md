---
type: post
title: "Site Launch :tada:"
description: A brief introduction to this blog, development process, and goals.
author: Eccentric J
author-email: jayzawrotny@gmail.com
location: New York, USA
date-created: 2019-01-18T13:20:00-5:00
date-modified: 2019-01-23T13:35:00-5:00
date-published: 2019-01-25T18:00:00-5:00
in-language: en
keywords: a,b,c
canonical-url: http://blog.hashobject.com/celebrare-gutture
uuid: a811549b-242e-46dc-8813-27c9d20e5a9d
tags:
 - blog
 - projects
---
# Why even make a blog in 2019?

While fruitless, I admit, but after receiving some encouragement to publish my lengthy reddit comments in functional programming boards, I needed a place to publish them. I wanted a place to explore the topics of functional programming and professional software engineering. Medium could work but I found myself slowed down by their editing experience and since I do have some design experience I would rather have a stronger visual identity to color my words.

# What will this blog cover?

This blog covers functional programming topics in languages like Clojure and ClojureScript. I may cover JavaScript, Elixir, Haskell, and Elm but I enjoy Clojure and ClojureScript the most.

I will also publish announcements of upcoming projects, tutorials\\learning resources, open-ended ideas, and opinions on professional practices.

# What is this blog made with?

This is a statically generated site written in Clojure using https://perun.io/ which is based on [Boot](https://github.com/boot-clj) build tasks. I really like the concept of building functionality by composing small tasks together so it felt like a natural fit. Development moved fast and I am happy with the result so far.

For example the production build pipeline looks like this:

```clj
(deftask build
  "Build the blog source and output to target/public"
  [e build-env BUILD-ENV kw    "Environment keyword like :dev or :production"]
  (let [prod? (= build-env :prod)]
   (pipeline
     (generate-site :prod? prod?)
     (seo-files prod?)
     (cljs :ids ["prod"])
     (sass :output-style :compressed :source-map false)
     (clean :exclude [#".git"])
     (target :no-clean true)
     (notify))))
```

# Are you really _eccentric_?

No. Probably not. It felt like a nice word to embody my awkwardness along with my love of programming, cats, atmospheric black metal, skulls, and occult imagery. While I&rsquo;m more of a straight arrow than someone riding the line between the accepted norms and taboos of society I can dream can&rsquo;t I? Who uses the internet to face the realities of who they actually are? That would be madness! :smiling_imp:
