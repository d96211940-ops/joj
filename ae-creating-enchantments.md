# Creating Enchantments

***

The enchantment creation process may seem difficult at first, but once you get the hang of it, it becomes much easier. Start by practicing with simple enchants before moving on to more complex creations.

### Starting Out

To begin creating enchantments, you need a basic understanding of the enchantment template. Here's a rundown of the simple formatting:

{% hint style="warning" %} <mark style="background-color:blue;">enchantment\_system\_name</mark> must be in latin alphabet (a-z), and in lower-case.
{% endhint %}

<pre class="language-yaml"><code class="lang-yaml"><a data-footnote-ref href="#user-content-fn-1">&#x3C;enchantment_system_name></a>:
  display: "&#x3C;enchantment_display>"
  description: "&#x3C;enchantment_description>"
  applies-to: "&#x3C;applies_to>"
  type: "&#x3C;triggers>"
  group: "&#x3C;group>"
  applies:
    - &#x3C;material>
  levels:
    &#x3C;level_number>:
      effects:
        - "&#x3C;effects>"
</code></pre>

### Example Enchantment:

```yaml
berserk:
  display: '%group-color%Berserk'
  description: |-
    Chance of strength
    and mining fatigue.
  applies-to: Swords, Axes
  type: ATTACK;ATTACK_MOB
  group: UNIQUE
  applies:
    - ALL_SWORD
    - ALL_AXE
  levels:
    '1':
      chance: 4
      cooldown: 8
      effects:
        - POTION:SLOW_DIGGING:0:60 @Attacker
        - POTION:INCREASE_DAMAGE:0:60 @Attacker
    '2':
      chance: 8
      cooldown: 7
      effects:
        - POTION:SLOW_DIGGING:0:80 @Attacker
        - POTION:INCREASE_DAMAGE:0:80 @Attacker
```

This is the basic template for creating enchantments. Let's go over each setting:

#### `<enchantment_name>`

This is the "backend" enchantment name, used in all commands and admin processes. The enchantment name must be written in lowercase.

#### `display`

This is the enchantment display, which appears in item lore, chat messages, and book formats. Color codes can be used with the display. Special placeholders include:

* `%group-color%`: Use the group's global color code.

#### `description`

This is the enchantment's description, used in various menus and book formats. It supports the "\n" character for splitting to a new line, and requires double-quotes (") instead of single ('). Color codes can also be used with the display.

#### `applies-to`

This is used in menus, book formats, and chat messages. It tells users which item category the enchantment can be applied to (e.g., 'Armour', 'Weapons', etc.). Note that `applies-to` is visual only and does not affect which items the enchantment can actually be added to.

#### `type`

This defines what triggers the enchantment. Enchantment triggers can be found [here](https://wiki.advancedplugins.net/abilities/triggers)

#### `group`

This sorts enchantments into specified groups from the `groups.yml` file. It also hints at which global color should be used for display. Group names should be in UPPER-CASE.

#### `applies`

This sets a specified list of items that the enchantment can be added to. It supports plugin shortcuts such as:

* `ALL_[material]`: Sets all specified material items (e.g., `ALL_SWORD` allows all types of swords).
* `[material]_ARMOR`: Supports all types of specified armor (can be used with the previous shortcut to create `ALL_ARMOR`, allowing all possible armor pieces).

**`effects`**

Effects are the abilities which enchantment has. You can read more about effects and see effects list [here](https://wiki.advancedplugins.net/abilities/effects)

### Other Options

There are a few other options for further customization when creating enchantments. These options are not required for basic enchantments:

```yaml
<enchantment_name>:
  display: "<enchantment_display>"
  description: "<enchantment_description>"
  applies-to: "<applies_to>"
  type: "<triggers>"
  group: "<group>"
  applies:
    - <material>
  settings:
    <setting>: <value>
  levels:
    <level_number>:
      cooldown: <number>
      chance: <number>
      conditions:
        - "<condition>"
      effects:
        - "<effects>"
```

### Example Enchantment:

```yaml
explosive:
  display: '%group-color%Explosive'
  description: Chance for arrows to explode.
  applies-to: Bow
  type: SHOOT;SHOOT_MOB
  settings:
    showActionBar: true
  group: UNIQUE
  applies:
    - BOW
  levels:
    '1':
      chance: 20
      cooldown: 7
      effects:
        - TNT @Victim
```

#### `settings`

This is used to dictate special settings for specific enchantments. A full list of settings can be found [here](https://ae.advancedplugins.net/enchantments/creating-enchantments/settings).

#### `cooldown`

This is the length of time (in seconds) between enchantment activations. The default value is 0 seconds if no cooldown is specified.

#### `chance`

This is the chance (0-100) of the enchantment activating when the trigger is activated. The default value is 100

**`conditions`**

* Require a certain placeholder to be parsed for enchant to activate
* Supports PAPI placeholders
* Have as many conditions as you want
* In depth guide on conditions can be found [here](https://wiki.advancedplugins.net/abilities/conditions)

[^1]: Must be in latin alphabet (a-z), and lower-case.
