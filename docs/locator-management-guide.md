# Locator Management Guide

Locators live under `src/main/resources/locators/<module>/<page>.json`.

Example:

```json
{
  "submit": {
    "type": "css",
    "value": "button[type='submit']"
  }
}
```

Dynamic XPath placeholders use `<<param>>`.

Prefer stable selectors in this order:

1. Test IDs
2. Accessible names and roles
3. Stable CSS selectors
4. XPath only when needed
