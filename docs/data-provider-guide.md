# Data Provider Guide

JSON test data lives under `src/test/resources/testData`.

Expected shape:

```json
{
  "records": [
    {
      "name": "example",
      "path": "/get"
    }
  ]
}
```

Supported dynamic tokens:

- `{{RANDOM_STRING}}`
- `{{CURRDATE}}`

Use `JsonSchemaValidator` to validate data files before broad usage.
