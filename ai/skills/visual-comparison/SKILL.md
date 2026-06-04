# Visual Comparison Skill

Use this skill when comparing a KMP Compose screen against a reference screenshot, Stitch/Pencil/Figma export or expected visual design.

## Use For

- screenshot-to-implementation comparison
- Stitch/Pencil/Figma reference comparison
- before/after visual regression checks
- screen migration quality checks
- spacing, typography, color, radius and layout hierarchy review

Do not use for business logic validation, persistence testing, broad architecture review or performance benchmarking unless requested.

## Inputs

| Input | Required |
|---|---|
| Reference image/design | Yes |
| Target screen/file | Yes |
| Actual screenshot | When available |
| Device/theme/font/locale constraints | Optional |
| Design tokens | Optional |

If no actual screenshot exists, review implementation against the reference and state that screenshot verification was not performed.

## Comparison Order

1. screen structure
2. layout hierarchy
3. spacing and alignment
4. component sizes
5. typography
6. colors
7. radius/borders
8. shadows/elevation
9. icons/images
10. interaction, selected, loading and error states

Do not start with tiny color differences when layout hierarchy is wrong.

## Tolerance

- 1-2 dp differences are usually acceptable.
- Design-system token differences are acceptable if consistent with the app.
- Major hierarchy, spacing or state differences are not acceptable.
- Exact pixel parity is required only when explicitly requested.

## Fix Rules

- Adjust layout before color.
- Adjust spacing before typography.
- Use design tokens first.
- Edit only the target screen or directly related token/component.
- Do not redesign, change business logic, change navigation or add dependencies.

## Verification

If tools are available:

1. Build the relevant target.
2. Run the app on emulator/device.
3. Navigate to the screen.
4. Capture actual screenshot.
5. Compare with reference.
6. Apply smallest safe visual adjustment.

If tools are unavailable:

1. Compare code and reference manually.
2. State screenshot verification was not performed.
3. List assumptions.

## Output

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
