# SpringPostgresDemo
Project made during learning Spring Boot.<br>
The main goal is to create the working database for simple online store model.
# Tech
- Java 11
- Spring
- SQL
- PostgreSQL
- Maven
# Endpoints

#### - POST /api/item/add<br>
  - parameters names:<br>
    - (String) name
    - (String) description
    - (Type: NEW or USED) condition
    - (BigDecimal) price
    - (String) location
  - returns:
    - Object Item

#### - PUT /api/item/modify/{id}<br>
  - parameters names:<br>
    - (Path Variable)(UUID) id
    - (String) name
    - (String) description
    - (Type: NEW or USED) condition
    - (BigDecimal) price
    - (String) location
  - returns:
    - Object Item

#### - DELETE /api/item/delete/{id}<br>
  - parameters names:<br>
    - (Path Variable)(UUID) id
  - returns:
    - boolean

#### - POST /api/item/image/add/{id}<br>
  - parameters names:
    - (Path Variable)(UUID) id
    - (String) imageUrl
  - returns:
    - Object Item

#### - PUT /api/item/image/modify/{id}<br>
  - parameters names:<br>
    - (Path Variable)(UUID) id
    - (String) oldImageUrl
    - (String) newImageUrl
  - returns:
    - Object Item

#### - DELETE /api/item/image/delete/{id}<br>
  - parameters names:<br>
    - (Path Variable)(UUID) id
    - (String) oldImageUrl
  - returns:
    - boolean

#### - GET /api/item/get/all
  - returns:
    - Map with Items

#### - GET /api/item/get/{id}
  - parameters names:<br>
    - (Path Variable)(UUID) id<br>
  - returns:
    - Object Item
  