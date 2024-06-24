# The Schelling Segregation Model

---

## Overview

- **Purpose**: Understand emergence of segregation through simple rules.
- **Creator**: Thomas Schelling, economist and sociologist.
- **Importance**: Illustrates macro-level segregation from micro-level preferences.

---
layout: image-right
image : /img/schelling_mid.png
---
## Model Basics

- **Agents**: Represented on a grid (<span class="color-red">■</span>, <span class="color-blue">■</span>).
- **Preferences**: Each agent desires a certain % of neighbors to be like itself.
- **Movement**: Agents move if unsatisfied (desired % not met). (<span class="color-red">✖</span>, <span class="color-blue">✖</span>) 

---
layout: image-right
image : /img/schelling_fin.png
---

## Dynamics

- **Iteration**: Repeatedly check and move agents until stable state.
- **Emergence**: Even mild preferences lead to significant segregation.
- **Visualization**: Grid updates show evolving segregation patterns.

---

## Applications

- **Urban Planning**: Understand residential segregation.
- **Social Dynamics**: Analyze group behaviors and spatial patterns.
- **Policy Implications**: Inform interventions to mitigate segregation.

---

## Code the Schelling Model on a grid graph

Fill the `TODO` comments in the code of the Schelling Model file : `src/org/graphstream/tutorial/schelling/Schelling.java`

