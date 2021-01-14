# Manger en famille

Dans le cadre de l'évènement Hoppening de Maître T, qui se tiendra du 20 juillet 2150 au 30 juillet 2150, voici l'API pour l'outil interne de gestion de restaurant, le Maître T.

Voici les fonctionnalités actuellement supportées par Maître T :
* Réservation des tables ([RSV](https://projet2020.qualitelogicielle.ca/carnet/1_rsv/))
* Distanciation sociale obligatoire ([DSO](https://projet2020.qualitelogicielle.ca/carnet/2_dso/))
* Gestion des ingrédients ([GLI](https://projet2020.qualitelogicielle.ca/carnet/3_gli/))
* Attention aux champignons ([AAC](https://projet2020.qualitelogicielle.ca/carnet/4_aac/))
* Déplacer l'Ouverture du Hoppening ([DOH](https://projet2020.qualitelogicielle.ca/carnet/5_doh))
* Gérer le matériel ([MAT](https://projet2020.qualitelogicielle.ca/carnet/6_mat))
* Gérer les chefs ([CHF](https://projet2020.qualitelogicielle.ca/carnet/7_chf))

## Pour débuter

Ces instructions vous permettrons de faire fonctionner une copie de ce projet sur votre poste de travail pour des fins de développement et de tests.

## Structure des modules

Il existe trois [modules maven](https://maven.apache.org/guides/mini/guide-multiple-modules.html) dans le projet : 

* `external-service-api` : API Externe utilisé par Maitre T et  `reservation-api`.
* `reservation-api` : le projet contenant Maitre T.
* `application` : permet de démarrer les 2 API simultanément. 

## Compilation

Pour compiler l'application, exécuter:
```
mvn clean install
```

## Exécuter l'application

Pour exécuter l'application, exécuter:
```
mvn exec:java -pl application
```
L'application sera alors accessible au lien: http://localhost:8181.

## Exécuter les tests

Pour exécuter l'application, exécuter:
```
mvn test
```

## Application construite avec

* [Maven](https://maven.apache.org/) - Dependency Management
* [Jetty](https://www.eclipse.org/jetty/) - Servlet Container
* [Jersey](https://jersey.github.io/) - Servlet API REST
* [Jackson](https://www.baeldung.com/jackson) - Serializer JSON

## Documentation API

### **Création**

**Requête**

POST ```/reservations```
```
{
  "vendorCode": "TEAM"::string,
  "dinnerDate": "2150-07-21T15:23:20.142Z"::string,
  "from": {
    "country": {
        "code": "CA"::string,
        "fullname": "CANADA"::string,
        "currency": "CAD"::string,
    },
    "reservationDate": "2150-05-21T15:23:20.142Z"::string
  },
  "tables": [
    {
         "customers": [
            { "name": "John", "restrictions": [] },
            { "name": "Jane", "restrictions": ["vegetarian"] }
         ]
      },
      {
         "customers": [
            { "name": "Roger", "restrictions": ["allergies"] },
            { "name": "Rogette", "restrictions": ["vegetarian", "illness"] }
         ]
      },
      ...
  ]
}
```

**Réponses**

HTTP 201 Created

Headers
```
Location: /reservations/<reservationNumber::string>
```
HTTP 400 Bad Request

Si la date d’achat est en dehors de la période d’achat (1er janvier 2150 au 16 juillet 2150 inclusivement).
```
{
  "error": "INVALID_RESERVATION_DATE"::string,
  "description" : "Reservation date should be between January 1 2150 and July 16 2150"::string
} 
```
HTTP 400 Bad Request

Si la date du repas est en dehors du grand Hoppening (20 juillet 2150 au 30 juillet 2150 inclusivement).
```
{
  "error": "INVALID_DINNER_DATE"::string,
  "description" : "Dinner date should be between July 20 2150 and July 30 2150"::string
} 
```
HTTP 400 Bad request

Dans le cas où la liste de tables ou leurs clients sont vides, cette erreur est lancée.
```
{
  "error": "INVALID_RESERVATION_QUANTITY"::string,
  "description": "Reservations must include tables and customers"::string
} 
```
HTTP 400 Bad Request

Si la réservation ne suit pas les contraintes de distanciation sociale suivantes:
- Les réservations sont limitées à six (6) clients.
- Les tables sont limitées à quatre (4) clients.
- Le restaurant peut accueillir un maximum de quarante-deux (42) clients par journée.

```
{
  "error": "TOO_MANY_PEOPLE"::string,
  "description": "The reservation tries to bring a number of people which does not comply with recent government laws."::string
}
```
HTTP 400 Bad Request

Si la réservation ne suit pas les contraintes alimentaires suivantes:
- Pour une date donnée, si un client a déjà réservé une table avec un menu qui contient des carottes, aucune réservation ne peut être faite à cette date avec la restriction allergies.
- Pour une date donnée, si un client a déjà réservé une table avec une restriction allergies, aucune réservation ne peut être faite à cette date avec un menu qui contient des carottes.
- Un client qui mange des tomates ne peut pas réserver dans les cinq (5) premiers jours du Hoppening.

OU la réservation empêche l'assignation des chefs pour la journée, de sorte qu'une restriction ne peut plus être prise en charge ou tous les chefs sont déjà assignés.

```
{
  "error": "TOO_PICKY"::string,
  "description": "You seem to be too picky and now, you cannot make a reservation for this date."::string
} 
```
HTTP 400 Bad request

Si un champ est manquant et pour les autres erreurs (ex. restrictions qui est invalide, etc.)
```
{
  "error": "INVALID_FORMAT"::string,
  "description": "Invalid Format"::string
} 
```
### **Demande**

**Requête**

GET ```/reservations/<reservationNumber::string>```

**Réponses**

HTTP 200 Ok
```
{
    "reservationPrice": 0.00::float,
    "dinnerDate": "2150-07-21T15:23:20.142Z"::string,
    "customers": [
          { "name": "Luce", "restrictions": ["allergies"] }, ...
    ]
}
```
HTTP 404 Not found

Si la réservation n’existe pas.
```
{
  "error": "RESERVATION_NOT_FOUND"::string,
  "description": "Reservation with number XX not found"::string
} 
```

### **Rapport d'ingrédients**

**Requête**

GET ```/reports/ingredients?startDate=2020-07-21&endDate=2020-07-23&type=total|unit```

**Réponses**

Le **totalPrice** indique le prix total.
- Dans le cas d’un ingrédient précis à 2$, si on en achète deux (2), le totalPrice est de 4$ et le quantity, 2.
- Dans le cas d’une date, le prix total est la somme de tous les coûts totaux des ingrédients.
- Dans le cas de &type=total, le totalPrice précis de cette réponse est la somme de tous les prix d’ingrédients.

**avec &type=unit**

Les résultats sont retournés en ordre de date, puis en ordre alphabétique.

HTTP 200 OK

```
{
  "dates": [
    {
      "date": "2020-07-20",
      "ingredients": [
        {
          "name": "Carrots",
          "totalPrice": 3.00::long,
          "quantity": 3.00::long
        }
      ],
      "totalPrice": 3.00::long,
    },
    {
      "date": "2020-07-21",
      "ingredients": [
        {
          "name": "Pork loin",
          "totalPrice": 5.00::long,
          "quantity": 2.00::long
        }
      ],
      "totalPrice": 5.00::long
    }
  ]
}
```

**Avec &type=total**

HTTP 200 OK

```
{
  "ingredients": [
    {
      "name": "Tuna",
      "totalPrice": 5.00::long,
      "quantity": 2.00::long
    },
    {
      "name": "Pork loin",
      "totalPrice": 3.00::long,
      "quantity": 3.00::long
    }
  ],
  "totalPrice": 8.00::long
} 
```
HTTP 400 Bad Request

Si une des deux dates (début ou fin) est à l’extérieur de la période du Hoppening. Aussi, si la endDate est avant la startDate. Aussi, si une des deux dates est manquante.
```
{
  "error": "INVALID_DATE"::string,
  "description" : "Dates should be between July 20 2150 and July 30 2150 and must be specified."::string
} 
```
HTTP 400 Bad request

Pour le type qui est invalide ou manquant.
```
{
  "error": "INVALID_TYPE"::string,
  "description": "Type must be either total or unit and must be specified."::string
} 
```

### **Rapport de matériel**

**Requête**

GET ```/reports/material?startDate=2020-07-20&endDate=2020-07-26```

**Réponses**

HTTP 200 OK

```
{
  "dates": [
    {
        "date": "2020-07-20",
        "bought": {
            "knife": 6,
            "fork": 6,
            "spoon": 6,
            "bowl": 6,
            "plate": 6,
        }
        "totalPrice": ...
    },
    {
        "date": "2020-07-21",
        "cleaned": {
            "knife": 6,
            "fork": 6,
            "spoon": 6,
            "bowl": 6,
            "plate": 6,
        }
        "totalPrice": ...
    },
    {
        "date": "2020-07-24",
        "cleaned": {
            "knife": 3,
            "fork": 3,
            "spoon": 3,
            "bowl": 3,
            "plate": 3,
        }
        "totalPrice": ...
    },
    {
        "date": "2020-07-25",
        "cleaned": {
            "knife": 6,
            "fork": 6,
            "spoon": 6,
            "bowl": 6,
            "plate": 6,
        ],
        "bought": {
            "knife": 3,
            "fork": 3,
            "spoon": 3,
            "bowl": 3,
            "plate": 3,
        },
        "totalPrice": ...
    },
    {
        "date": "2020-07-26",
        "cleaned": {
            "knife": 3,
            "fork": 3,
            "spoon": 3,
            "bowl": 3,
            "plate": 3,
        },
        "totalPrice": ...
    }
  ]
}
```

HTTP 400 Bad Request

Si une des deux dates (début ou fin) est à l’extérieur de la période du Hoppening. Aussi, si la endDate est avant la startDate. Aussi, si une des deux dates est manquante.

```
{
  "error": "INVALID_DATE"::string,
  "description" : "Dates should be between July 20 2150 and July 30 2150 and must be specified."::string
}
```

### **Rapport des chefs**

**Requête**

GET ```/reports/chefs```

**Réponses**

HTTP 200 OK

```
{
  "dates": [
    {
       "date":"2020-07-20",
        "chefs": [
            "Amélie Bacon",
            "Boris Vian"
        ],
        "totalPrice:" 12000
    },
    {
        "date": "2020-07-21",
        "chefs": [
            "Doris Fruitier"
        ],
        "totalPrice:" 6000
    },
        "date": "2020-07-24",
        "chefs": [
            "Alexandre Boyardi",
            "Céline Vanyle",
            "Diane Fromaj"
        ],
        "totalPrice:" 18000
    },
    ...
}
```

### **Rapport des totaux**

**Requête**

GET ```/reports/total```

**Réponses**

HTTP 200 OK

```
{
    "expense": 0::long,
    "income": 0::long,
    "profits": 0::long
}
```

### **Configuration du Hoppening**

**Requête**

POST ```/configuration```

Permet de modifier la durée et le moment de l’activité d’ouverture. Cet appel permet également de modifier la plage de temps qui permet de réserver sa table au restaurant.

**Réponses**

HTTP 200 OK

```
{
     "reservationPeriod": {
         "beginDate": "2150-02-20",
         "endDate": "2150-02-23"
     },
     "hoppening": {
         "beginDate": "2150-03-20",
         "endDate": "2150-03-23"
     }
 }
```

HTTP 400 Bad Request

Dans le cas où les plages se chevauchent ou si la réservation est avant le Hoppening.

```
{
  "error": "INVALID_TIME_FRAMES"::string,
  "description": "Invalid time frames, please use better ones."::string
} 
```

HTTP 400 Bad Request

Dans le cas où les dates sont invalides.

```
{
  "error": "INVALID_DATE"::string,
  "description": "Invalide dates, please use the format yyyy-mm-dd"::string
} 
```

HTTP 400 Bad Request

Dans le cas où le JSON est invalide.

```
{
  "error": "INVALID_FORMAT"::string,
  "description": "Invalid format"::string
}  
```