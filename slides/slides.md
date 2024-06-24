---
theme : ./
---

<p class="text-[70px] leading-[1.4] font-bold"> Dynamic Graphs </p>

<p class="text-[40px] "> Practice Session </p>

<p class="text-[40px] leading-[1.4] "> Graphadon Summer School </p>
<p class="text-[30px] "> June 24-29, 2024 </p>

---
layout: two-cols
---

<p class="text-4xl leading-[1.4]">Resources</p>

Code : [github.com/graphstream/graphadon](https://github.com/graphstream/graphadon)

Slides : [graphstream.github.io/graphadon](https://graphstream.github.io/graphadon)



::right::
<div class="flex flex-col items-center">
<QRCode
    :width="300"
    :height="300"
    type="svg"
    data="https://github.com/graphstream/graphadon"
    :margin="10"
    :imageOptions="{ margin: 10 }"
    :dotsOptions="{ color: '#000' ,type: 'rounded'}"
    image="/img/gs-logo.svg"
/>
</div>
<!-- ![Sources for Codes and Presentations](/img/qr-graphstream.github.io-gs-talk.svg) -->

---

<p class="text-4xl leading-[1.4]">Outline</p>

<Toc maxDepth=1 />



---
src: ./intro.md
---

---
src: ./tutorials.md
---

--- 
src: ./communities.md
--- 

---
src: ./schelling.md
---