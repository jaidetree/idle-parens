---
type: post
title: "My Functional Programming Experience"
description: I climbed the mountain of madness only to find the peaks smell a lot like functional programming.
author: Eccentric J
author-email: jayzawrotny@gmail.com
location: New York, USA
date-created: 2019-01-23T14:02:00-5:00
date-modified: 2019-01-29T13:52:00-5:00
date-published: 2019-01-28T19:31:00-5:00
headline:
in-language: en
keywords: a,b,c
uuid: db151ec4-50b8-42f9-b20e-2153cb5d93f2
tags:
 - clojure
 - elixir
 - experience
---
# Discovering Functional Programming

I first encountered the phrase "Functional Programming" when learning about Redux for JS. My first question was "Isn’t all programming functional?" then I began researching it and got genuinely curious and found the motivation to study it further. Now about 2 years into it I can confirm it has deeply impacted how I think, work, and the trajectory of my career.

When I started learning I was really in the weeds with OOP and wanted to abstract, generalize, and use all those fancy OOP patterns everywhere it made sense. The problem is whenever you create a custom class in most languages, it’s incompatible with everything else. Even in JS if I extend an array class it’s often treated as a separate type. So you often have to write more classes to make two classes talk to each other.

In addition to those layers of abstraction for OOP I think there’s no upper limit to the complexity you can achieve and the vocabulary to describe OOP systems. Getters, setters, public, private, accessors, attributes, methods, virtual properties, constructor overrides, generics, magic methods, over loading, dependency injections, super, child,  inheritance, the list goes on I’m sure.

With functional programming I find it has a pretty fixed amount of concepts that make up the methodology:

* Immutability
* Pure Functions
* Referential Transparency
* Composition
* Higher Order Functions
* Core Data Types
* Functors
* Monads
* Point Free
* Predicates
* Map, Filter, Reduce functions

That may not be every concept but it is more than enough to build a strong sense of competency with the methodology.

What’s great about learning those is that they all build on each other where as a lot of those OOP concepts are kind of parallel to each other where you can learn one without knowing the other.

## Types of Functional Programming Languages

There’s two major camps that functional programming falls into:

### Dynamic Languages

Languages like Elixir and Clojure fall into this camp. They leverage dynamic types and focus on creating pipelines of functions to transform data. These languages also tend to be more side-effect friendly with looser restrictions but agreed upon naming conventions to denote when side effects happen. Generally these languages have a more practical nature to them focusing on getting work done.

### Static Languages

Languages like Elm, F#, OCaml or Haskell fall into this camp. They emphasize type theory and other mathematic principles to leverage monads, or data types that represent computation builders to do most of the work. For instance the Array type in JS is a monad, it can be mapped to an array of other values, packed, and unpacked in itself. These languages are more strict about side effects often requiring them to be wrapped in a IO monad or similar system to separate out side-effect work from pure functions. While they can be used for practical work they often have an academic background and require more specification in determining the correctness of a program. For instance your program might not compile until you specify which types a function receives and returns.

#### Other Languages

Languages like JS, Ruby, and Python have some functional capabilities but I don’t consider them functional programming languages for the sake of this discussion. You can of course apply functional programming concepts to or within these languages.

#### Comparison

In so far I don’t strongly feel one camp is better than another, I’ve just noticed that functional programming languages tend to lean to one side more than the other. While I’ve chosen to focus on Clojure and Elixir for now, I’m also interested in learning and hearing about Haskell and Elm.

## Does functional programming make you a better programmer?

As far as improving as a programmer and engineer: I think putting in the time and effort to learning it will impact you for the better. I’ve learned how to reduce complexity in the work I produce by composing larger systems from smaller functions that have very specific jobs, often aimed at expressing a domain-specific goal with pure functions as much as possible. This means fewer surprises for other programmers and stronger testability because testing pure functions like `add(1, 2)` is much easier than testing classes, instances, and runtime app state like `this.router.handleRoute(window.location)`.

Instead of trying to make more complex data types like I did when strictly using OOP, I find myself mostly leaning on core types. Elixir is a great example; Elixir has hundreds of functions that can work on lists, maps, streams, and files. It has zero that work on that `Bid` class we created at my day job.

## Contributing to Major Libraries

I’ve found myself able to move faster with FP languages as well, I’m able to read the source of major libraries in languages like Clojure and make contributions despite only having about a year of experience with it.

After using JS for a decade at this point there are many libraries I can barely make heads or tails of. They’re not bad, it’s just more difficult for me to understand the intention of the code and find room for improvement. Much of what’s out there is very side-effect driven and organized in larger chunks of business logic. For instance at work our `Bid` class has methods like `accept` which updates db state of several models, sends several emails to clients and partners, and populates another dependent model representing proposal data.  It feels like a lot but I’m unsure what the purpose of the method actually is.

## Modeling and Universal Principles

Lastly, I like how you can model real-world domains in FP. It feels more intuitive to me and I think is easier to explain to other people outside of the tech industry. [https://pragprog.com/book/swdddf/domain-modeling-made-functional](https://pragprog.com/book/swdddf/domain-modeling-made-functional) is a great book on that topic and raises a good point that OOP does miss the mark in a lot of places, especially when you inevitably need classes like BidManager or PageFactory that don’t really map to anything in the domain.

My favorite part about learning FP is how universal those principles are, I can apply them to Elixir, Clojure, Python, Ruby, PHP, JS, and I’m sure even more languages out there. Transitioning between functional programming languages feels like it’s just a matter of learning the syntax.

## Visualizing Functional Programming

A metaphor I really like to use is the Lego Great-Ball-Contraptions:
<iframe
  width="560"
  height="315"
  src="https://www.youtube.com/embed/Kp62YVtejiY"
  frameborder="0"
  allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
  allowfullscreen></iframe>

You have many different machines (functions) that operate on balls (common data types). In OOP the equivalent would be wrapping each ball in a new container so another machine can work with it then either unwrapping it or wrapping another container over the ball’s container for the next machine.

## OOP is not evil but what we have done with it could be

For the record I don’t despise OOP or think FP itself is what lead to my improvement, it’s more that I gained a new way of thinking and solving problems. I think there are contexts where OOP is the right choice (especially game programming) but there’s a lot of places where we reach for OOP or imperative programming that could be better tackled with a FP or FP-inspired approach.

<blockquote class="disclaimer">

  **Disclaimer**: Originally my response to a reddit question:
  [Is it true that learning functional programming makes you a better programmer?](https://www.reddit.com/r/elixir/comments/ac7m4t/is_it_true_that_by_learning_functional_language/)

</blockquote>
