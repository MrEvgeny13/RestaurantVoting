# Voting System for restaurants

**Topjava's graduation project.**

Designing and implementing a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) *without frontend*.

Build a voting system for deciding where to have lunch.

- 2 types of users: admin and regular users
- Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
- Menu changes each day (admins do the updates)
- Users can vote on which restaurant they want to have lunch at
- Only one vote counted per user
- If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

---

### Recommendations for Tomcat:
In Application context: **/voting**     
URL after launch: **http://localhost:8080/voting/rest/admin/users/**

---
### Initial User data:

| User          | Mail:Password          | 
| ------------- |:------------------:    | 
| Admin         | admin@mail.ru:password |  
| User          | user@mail.ru:password  |    
| User2         | user2@mail.ru:password |    

---

## Curl samples: 


### *Users with the Admin role:*
#### Get all Users:
curl -s http://localhost:8080/voting/rest/admin/users --user admin@mail.ru:password

#### Get User 100000:
curl -s http://localhost:8080/voting/rest/admin/users/100000 --user admin@mail.ru:password

#### Get User by email user2@mail.ru:
curl -s http://localhost:8080/voting/rest/admin/users/by?email=user2@mail.ru --user admin@mail.ru:password

#### Create User:
curl -s -X POST -d '{"name":"NewUser123", "email":"user123@mail.ru", "password":"password", "roles":["ROLE_USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/admin/users --user admin@mail.ru:password

#### Delete User 100000:
curl -s -X DELETE http://localhost:8080/voting/rest/admin/users/100000 --user admin@mail.ru:password

#### Update User 100019:
curl -s -X PUT -d '{"name":"NewUser777", "email":"user777@mail.ru", "password":"password123"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/admin/users/100019 --user admin@mail.ru:password

---

### *New User:*
#### Create User (register):
curl -s -X POST -d '{"name":"NewUser123", "email":"user123@mail.ru", "password":"password123"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/profile/register

---

### *Users with the User role:*
#### Get profile:
curl -s http://localhost:8080/voting/rest/profile --user user2@mail.ru:password

#### Update profile:
curl -s -X PUT -d '{"name":"User", "email":"user@mail.ru", "password":"password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/profile --user user2@mail.ru:password

#### Delete profile:
curl -s -X DELETE http://localhost:8080/voting/rest/profile --user user123@mail.ru:password123

---

### *Restaurants with the Admin role:*
#### Get Restaurants:
curl -s http://localhost:8080/voting/rest/admin/restaurants --user admin@mail.ru:password

#### Create Restaurant:
curl -s -X POST -d '{"name":"Test Restaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/admin/restaurants --user admin@mail.ru:password

#### Update Restaurant 100021:
curl -s -X PUT -d '{"name":"Updated Restaurant"}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/admin/restaurants/100021 --user admin@mail.ru:password

#### Delete Restaurant 100021:
curl -s -X DELETE http://localhost:8080/voting/rest/admin/restaurants/100021 --user admin@mail.ru:password

---

### *Restaurants with the User role:*
#### Get all Restaurants for today:
curl -s http://localhost:8080/voting/rest/restaurants --user user@mail.ru:password

#### Get Restaurant 100004 for today:
curl -s http://localhost:8080/voting/rest/restaurants/100004 --user user@mail.ru:password

#### Get Restaurant 100005 with a dish for the date 2021-01-10 (you can request the current date):
curl -s http://localhost:8080/voting/rest/restaurants/100005?date=2021-01-10 --user user@mail.ru:password

#### Get all Restaurants with a dish for the date 2021-01-10 (you can request the current date):
curl -s http://localhost:8080/voting/rest/restaurants?date=2021-01-10 --user user@mail.ru:password

---

### *Dish with the Admin role:*
#### Get all Dishes for the Restaurant 100005:
curl -s http://localhost:8080/voting/rest/admin/restaurants/100005/dishes --user admin@mail.ru:password

#### Get Dish 100011 from the Restaurant 100005:
curl -s http://localhost:8080/voting/rest/admin/restaurants/100005/dishes/100011 --user admin@mail.ru:password

#### Create Dish in the Restaurant 100005 (you can request the current date):
curl -s -X POST -d '{"name":"Test dish","price":99,"date":"2021-01-10"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/admin/restaurants/100005/dishes --user admin@mail.ru:password

#### Update Dish 100022 in the Restaurant 100005 (you can request the current date):
curl -s -X PUT -d '{"name":"Updated Dish","price":199,"date":"2021-01-10"}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/admin/restaurants/100005/dishes/100022 --user admin@mail.ru:password

#### Delete Dish 100022 in the Restaurant 100005:
curl -s -X DELETE http://localhost:8080/voting/rest/admin/restaurants/100005/dishes/100022 --user admin@mail.ru:password

---

### *Voting with the Admin role:*
#### Get all Votes for today:
curl -s http://localhost:8080/voting/rest/admin/users/votes/ --user admin@mail.ru:password

---

### *Voting with the User role:*
#### Vote for the Restaurant 100005:
curl -s -X POST -d '{"restaurantId":"100005"}' -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/votes --user user@mail.ru:password

#### Change Vote for the Restaurant 100005:
curl -s -X PUT -d '{"restaurantId":"100005"}' -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/votes/100018 --user user@mail.ru:password

#### Get Votes for today:
curl -s http://localhost:8080/voting/rest/votes/ --user user@mail.ru:password

#### Filter Votes (you can request the current date in "endDate"):
curl -s "http://localhost:8080/voting/rest/votes/filter?startDate=2020-12-01&endDate=2021-01-10" --user user@mail.ru:password
