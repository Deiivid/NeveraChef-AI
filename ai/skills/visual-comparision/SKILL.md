# Visual Comparison Skill

Use this skill when comparing a generated KMP Compose screen against a reference screenshot, Stitch/Pencil/Figma export or expected visual design.

Visual comparison means checking whether the implemented screen visually matches the reference, then identifying the smallest safe UI adjustments.

---

## When to use this skill

Use for:

- screenshot-to-implementation comparison
- Stitch/Pencil/Figma reference comparison
- before/after visual regression checks
- checking screen migration quality
- checking spacing, typography, colors, radius and layout hierarchy

Do not use for:

- business logic validation
- persistence testing
- broad architecture review
- performance benchmarking unless explicitly requested

---

## Inputs

| Input | Required | Notes |
|---|---|---|
| Reference image | Required | Screenshot/design/mockup to match. |
| Actual image | Required when available | Screenshot captured from emulator/device. |
| Target screen | Required | Screen or file being checked. |
| Known constraints | Optional | Device size, theme, font scale, locale, platform. |
| Design tokens | Optional | Useful for expected spacing/color/typography. |

If no actual screenshot exists, review the implementation against the reference and state that screenshot verification was not performed.

---

## Comparison order

Compare in this order:

1. screen structure
2. layout hierarchy
3. spacing and alignment
4. component sizes
5. typography
6. colors
7. radius/borders
8. shadows/elevation
9. icons/images
10. interaction/selected/loading/error states

Do not start with tiny color differences if the layout hierarchy is wrong.

---

## Difference categories

| Category | Examples |
|---|---|
| Layout | Wrong hierarchy, wrong alignment, wrong distribution. |
| Spacing | Padding/gap/margin mismatch. |
| Size | Wrong height, width, icon size, card size. |
| Typography | Wrong font size, weight, line height or text style. |
| Color | Wrong background, text, border or state color. |
| Shape | Wrong corner radius or border. |
| Elevation | Missing or excessive shadow. |
| Content | Missing text, icon, image or state. |
| Behaviour | Selected/loading/error state does not match. |

---

## Tolerance

Use practical tolerance:

- 1-2 dp differences are usually acceptable.
- Design-system token differences are acceptable if consistent with the app.
- Major hierarchy, spacing or state differences are not acceptable.
- Exact pixel parity is not required unless explicitly requested.

---

## Correction rules

When fixing differences:

- adjust layout before color
- adjust spacing before typography
- use design tokens first
- avoid hardcoded values if project tokens exist
- edit only the target screen or directly related design token/component
- do not redesign
- do not change business logic
- do not change navigation
- do not introduce dependencies

---

## Verification workflow

If tools are available:

1. Build the relevant target.
2. Run the app on emulator/device.
3. Navigate to the target screen.
4. Capture actual screenshot.
5. Compare with reference.
6. Apply smallest safe visual adjustments.
7. Repeat only if necessary.

If tools are not available:

1. Compare code and reference manually.
2. State that screenshot verification was not performed.
3. List assumptions.

---

## Output

Return:

```text
Visual comparison:
- Match level: High/Medium/Low
- Main differences:
  - ...
- Suggested fixes:
  - ...

Validation:
- ...

Notes:
- ...
```
