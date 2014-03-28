---
layout: page
title: Release Notes
header: Pages
group: navigation
---
{% include JB/setup %}

{% for post in site.posts %}
  {% include JB/blog_list %}
{% endfor %}