
It is an Android Application developed using Android Studio. I developed the front-end using XML and back-end using JAVA. Server-side is developed in Netbeans and it interacts with the client-side using REST architecture. Data is stored on Firebase cloud. Some part of the data is stored locally in SQLite database. This feature has been developed using the observer pattern and livedata. This application has following features:

(i) Users can sign-in and sign-up (there are various validations).
(ii) The users can search any movie using the search bar (this functionality has been developed using the TMDb API). After the results are retrieved, relevant information of the search movie is shown (title, releasedate, poster etc).
(iii) Once the movie is clicked, more information about the movie is shown (summary, directory, public rating etc). Users can either add this movie to their memoir(if they have watched it), or to their watchlist (if they want to watch that movie in the future). If they click on 'Add to Memoir', then the user will be further asked about the memoir details (cinema details etc), and if they click on 'Add to Watchlist', then the movie will be stored in SQLite as well as Firebase cloud and the user will be shown a real-time update.
(iv) The user can also view various reports (bar chart and pie chart) which will reveal information about how many movies were watched by the users in which theatres in the past year.
(v) So as to ease the process of choosing a cinema, nearby cinemas will be shown to the user on google maps.

