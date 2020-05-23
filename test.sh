# ---------- Test product service ----------

# Add some products
curl -i -H "Content-Type:application/json" -d '{"name": "Fossil Q", "price":"100", "brand": "fossil", "color":"red"}' http://localhost:8080/products
curl -i -H "Content-Type:application/json" -d '{"name": "Fossil Q", "price":"100", "brand": "fossil", "color":"blue"}' http://localhost:8080/products
curl -i -H "Content-Type:application/json" -d '{"name": "Skagen Sport", "price":"120", "brand": "skagen", "color":"red"}' http://localhost:8080/products
curl -i -H "Content-Type:application/json" -d '{"name": "Skagen Sport", "price":"120", "brand": "skagen", "color":"blue"}' http://localhost:8080/products

# Get all products
curl -i http://localhost:8080/products
# Get all products, paging with size of each page
curl -i http://localhost:8080/products?size=2
# Get all products, paging with size of each page, and specific page (page 0 & page 1)
curl -i http://localhost:8080/products?page=1&size=2
# Get all products, sort by color in desc
curl -i http://localhost:8080/products/?sort=color,desc

# Find products by name
curl -i http://localhost:8080/products/search/findByName?name=Fossil%20Q
# Find products by name
curl -i http://localhost:8080/products/search/findByBrand?brand=fossil
# Find products by name
curl -i http://localhost:8080/products/search/findByColor?color=red
# Find products by price between (priceLow, priceHigh)
curl -i "http://localhost:8080/products/search/findByPriceBetween?priceLow=100&priceHigh=110"

# Get all audit logs
curl -i http://localhost:8080/logEntries
# Manually insert 1 audit log entry
curl -i -H "Content-Type:application/json" -d '{"timestamp": "2020-05-21 10:00:00.123", "severity":"INFO", "service": "product", "action":"findAll", "result":"200 OK"}' http://localhost:8080/logEntries

# ---------- Test user service ----------

# Get all user profiles
curl -i http://localhost:8081/users
# Manually insert 1 user profile
curl -i -H "Content-Type:application/json" -d '{"fbId": "1234567890", "firstName":"Marlin", "lastName": "Le", "gender":"MALE", "age":37}' http://localhost:8081/users
