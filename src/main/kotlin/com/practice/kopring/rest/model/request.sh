curl --request POST \
  --url http://localhost:8080/createPerson \
  --header 'Content-Type: application/json' \
  --data '{
	"name": "Kim",
	"age": 20,
	"carList": [ { "name": "BMW", "color": "blue" }, { "name": "Benz", "color": "black" }]
}'
