# book_club
Book Club App Software Development

## How to run each component
Once the repository has been downloaded on the local machine, open up the specific app you want to run by opening up android studio and opening the file along the path that you want to run. I.e. book_club for the book club app. The folder which contains the correct part to run should have an android logo attached to it like this.  ![image](https://github.com/Sayonaru/book_club/assets/86726414/8990d3f0-769a-4988-94a6-964fe0035a45)

Once the specific app file has opened in android studio, wait until it has completely compiled. Once available, the green start button will open an emulator to run the app. **Be Advised** this app was built with the **Pixel 3A** in mind and since this is a software heavy project and not worried about UI, will run at its best with this emulator. ![image](https://github.com/Sayonaru/book_club/assets/86726414/dc86e21e-764c-4ed2-95e7-2ff545655124)


## How to use the app
Start by creating an account by signing up. Once you have created an account, you will be signed into the main screen. \
The logout button is positioned at the top left where it will properly log the user out and return to the splash screen again. ![image](https://github.com/Sayonaru/book_club/assets/86726414/96f8b83f-d22c-4dbb-85f5-720d7fa13bd2)

The saved books and forums tabs should display the books that the user has saved but aren't displaying correctly at the moment. The progress bars will be present while the recycler view tries to load the correct stuff, in practice will disappear if it finds nothing or is displaying something. ![image](https://github.com/Sayonaru/book_club/assets/86726414/98d350cf-bc51-4da1-9f43-1048cb5057a8)

Clicking the **Search Book** button will bring the user to the search screen where typing the name of an existing book should bring up that book if in the google book api. Pressing the search icon on the right hand side will initialise the search. ![image](https://github.com/Sayonaru/book_club/assets/86726414/22e9bda7-d3d2-4632-b7f2-276d414d82c1)

A list of the books with the corresponding item you searched should appear with basic details about the book. \
Upon clicking a specific book, a few more details will be present with a few options to choose from. \
![image](https://github.com/Sayonaru/book_club/assets/86726414/aa617902-c828-4589-90dd-9d096ff4cb0e)

Clicking the preview button will send the user to the preview page of the book supplied by google books. \
![image](https://github.com/Sayonaru/book_club/assets/86726414/0a27d27e-7d44-4e1e-85b5-20e7e2e1dd0a)

Clicking the buy button will send the user to the buy page of the book if one is present, else it will tell the user otherwise. \
![image](https://github.com/Sayonaru/book_club/assets/86726414/4f717c7d-9abc-4610-beb7-f64bc177c04a)

The "Fav" button will save that book for the user for them to easily access it from the main page. Once clicked on the main page, will open this page back up again for them to quickly see the forum page. \
The Forum button will open up the forum page of that book which will have all the messages sent in that specific forum from all users who have participated. \
![image](https://github.com/Sayonaru/book_club/assets/86726414/a27526b5-99fd-4613-b706-647b444817d3)

The review button to the right of the reviews title will initiate a popup to allow the user to type their review which will be displayed at the bottom of the screen. \
![image](https://github.com/Sayonaru/book_club/assets/86726414/28d29304-fdbe-4c8a-a165-73529d954bf3)
\
If you click on the review and it is yours, you will be given the option to delete the review. \
![image](https://github.com/Sayonaru/book_club/assets/86726414/18a02e7f-c4f1-4a0a-9518-d44e54e31143)


## This project & the future
This book club app project is primarily made around showcasing software skills involving android java development. Utilising database handling, data validation, adapters, reading, storing and displaying data input by the user and employing group chat like scenarios. The UI had many problems and not enough time to fix, but should be easily fixable with adequate time to look over the errors and employ the steps necessary to fix them if I were to continue this project in the future. \
There would also be a few more features I would have liked to add such as admin features which would allow them to delete any message from reviews or forums that they deem inappropriate. This would be achieved by manipulating the user type data in the database to admin and when the user tries to delete the message, while looking if the uid match, also check if the current user type is an admin. This feature can be mimiced for other admin features they will have across the app. To keep the admin feature contained, the database owner would manually give out this role. I believe this to be the most efficient as a starting point as there should be too many admins and it is a powerful role to be given out by accident.
For adding books, since I have ran into a few books which do exist but aren't on the api, including a way to manually add books would be easy at it would be done the same as in the database session activity and similar to adding comments and messages in the forum. These would primarily be updated to the firebase database, however I believe that having the ability to create books offline would be good which can be saved to an SQLite database. These can then be checked if they are added to the online database everytime the user logs in if they have internet connection. If the bookId is already in the firebase database do nothing, else add it to the database.

## Database
The database is split up into users and books \
![image](https://github.com/Sayonaru/book_club/assets/86726414/66c41ad3-abb8-4930-9ce0-7224836b0a3c)
\
The reviews are saved under the bookId, Comments, a timestamp id, and then the relevant info for them to be displayed back to the user \
![image](https://github.com/Sayonaru/book_club/assets/86726414/477f5cfc-e9dc-4648-8379-4d066d74e68a)
\
The forum messages are saved in similar fashion under timestamp ids \
![image](https://github.com/Sayonaru/book_club/assets/86726414/98e469b1-cce9-44ed-b1c8-8e72f77e4101)
\
The rest of the book info is stored so that it can be displayed to the front screen and other UI needs \
![image](https://github.com/Sayonaru/book_club/assets/86726414/b1510be9-af6a-4414-bfe9-dbf6b4b2fc0b)
\
The user record consists of which bookId they have favourited, personal info for the app, and userType which will be either user or admin to access certain extra features to help with moderation within the app. \
![image](https://github.com/Sayonaru/book_club/assets/86726414/412524a5-5e1d-4dec-a7dd-6b9176e3d296)


## Known Errors
Strictly UI based. Saved books and forums the user has engaged in are not appearing on the main screen.
The images are not being displayed correctly when the thumbnail is retrieved from the google api. Picasso seems to be having trouble compiling the thumbnail retrieved to display it in the image boxes.

