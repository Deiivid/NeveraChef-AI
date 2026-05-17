# DESIGN_SYSTEM

Last updated: 2026-05-15

## 1. Source and confidence

Confirmed by Pencil handoff:

- Palette, typography intent, and component direction.

Confirmed by repo:

- Theme implementation is still template level.

Therefore:

- These tokens are approved as v1 baseline and should be treated as living design tokens.

## 2. Core color tokens

- `NeveraChefBlue = #0066FF`
- `NeveraChefInk = #1A1A1A`
- `NeveraChefMuted = #666666`
- `NeveraChefLine = #E6E8EC`
- `NeveraChefSoft = #F6F8FB`
- `NeveraChefAccentSoft = #EAF2FF`
- `NeveraChefWarning = #FFF4D6`
- `NeveraChefErrorSoft = #FFE7E2`
- `NeveraChefSuccessSoft = #EAF7EF`

## 3. Typography

- Heading: Inter
- Body: Geist
- Caption/UI compact: Funnel Sans

Fallback rule:

- If font packaging is not ready for MVP start, use temporary system fallback and document it explicitly in PR/commit notes.

## 4. Spacing and sizing baseline

- Base spacing unit: 4dp
- Common spacing steps: 4, 8, 12, 16, 20, 24, 32
- Primary CTA minimum height: 52dp
- FAB minimum: 56dp
- Action card minimum: 64dp to 74dp

## 5. Shape and border rules

- Cards: medium corner radius (12-16dp target)
- Pills/chips: high radius
- Dividers and low-emphasis borders use `NeveraChefLine`

## 6. Component foundations

Buttons:
- Primary button: blue fill + white text.
- Secondary button: neutral/outline style.

Cards:
- Use clear hierarchy: title > metadata > action.
- Avoid dense copy blocks.

Inputs:
- Clear labels.
- Error text visible and explicit.

States:
- Loading, Empty, Error components are mandatory and reusable.

## 7. Accessibility rules

- Never encode status by color only.
- Keep high contrast text on light surfaces.
- Add `contentDescription` for actionable icons.
- Respect dynamic text sizing and avoid fixed-height text containers where possible.

## 8. Android and iOS differences

Default:
- Shared Compose UI for both platforms for MVP consistency.

Allowed divergence (future):
- Native wrappers and platform-specific affordances if UX benefit is clear and documented.

## 9. Pending visual confirmations

1. Final icon set source.
2. Final typography packaging strategy.
3. Dark theme policy (currently not MVP requirement).
