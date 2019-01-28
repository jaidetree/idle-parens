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

Likely lost in the void, I know, but I want to share some thoughts on functional programming and professional software engineering that mean a lot to me. Kind reddit commenters have unexpectedly encouraged me to publish some of my meatier posts in functional programming subreddits. Now I have a place to publish them to.

# What will this blog cover?

This blog covers functional programming topics in languages like Clojure and ClojureScript. I may cover JavaScript, Elixir, Haskell, and Elm, but I intend to focus on Clojure and ClojureScript more since I enjoy working with them the most.

I will also publish announcements of upcoming projects, tutorials\\learning resources, open-ended ideas, and opinions on professional practices.

# What platform should I use?

This took a long time to decide. Wanting to tryout a [JAMstack](https://jamstack.org/) a statically generated site felt the most enticing. Hugo and Jekyll seem really nice but I know I would be more motivated to keep working on it if it were in Clojure.  For building the static files I chose https://perun.io/ which is based on [Boot](https://github.com/boot-clj) build tasks. I really like the concept of composing small tasks together to form larger bodies of functionality so it felt like a natural fit. Development moved fast and I am happy with the result so far. Now I have this powerful framework I can keep improving over time without investing too much time to bring ideas to life.

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
The rest of the source is available at <a class="brand fa-github" href="https://github.com/eccentric-j/idle-parens">eccentric-j/idle-parens</a> if you are the slightest bit curious how it all came together.


# Are you really _eccentric_?

No. Probably not. It felt like a nice word to embody my awkwardness along with my love of programming, cats, black\\death metal, skulls, and occult imagery. While I&rsquo;m more of a straight arrow than someone riding the line between the accepted norms and taboos of society I can dream can&rsquo;t I? Who uses the internet to face the realities of who they actually are? That&rsquo;s madness! :smiling_imp:
