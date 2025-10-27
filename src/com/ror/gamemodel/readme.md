base game attributes go here. characters, etc.

Entity.java // base class for every entity that can fight (chars, generals, boss)
--> (?) PlayerEntity.java // extends from character.java, adds player-specific data maybe
--> Enemy.java // extends from character.java. adds hostile npc behavior
Skill.java // base for skills and ults. incl name, damageType, cost, cd
StatusEffect.java // handling for applied effects (burnDamage, shield, etc)
StatBlock.java // holds numeric stats (atk, def, hp etc)

test classes:
Andrew.java -> main protag
Goblin.java -> base enemy
