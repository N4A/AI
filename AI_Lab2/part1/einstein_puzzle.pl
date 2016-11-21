%(1)There are five houses of different colors next to each other on the same road. In each house lives
%a man of a different nationality. Every man has his favorite drink, his favorite brand of cigarettes,
%and keeps pets of a particular kind.

%this message tells us there are a commen templete of the five houses and many impilct messages
%like the order between houses.
%So,I decide to build five house of which the templete is as follows,
%and then use the following message to fill the blank.
%then, I can get five house full of message.

%Templete: house(Color,People,Drink,Cigarette,Pet)/5 and they should orderd in the list

%all houses
houses(H):-
	
	%init five blank houses
	H=[_,_,_,_,_],
	
	%members' features.	
	%(10) The Norwegian lives in the first house.(put 10 here is for optimization)
	H = [house(_,norwegian,_,_,_)|_],
	
	%(15) The Norwegian lives next to the blue house.(put 15 here is for optimization)
	neibour(house(_,norwegian,_,_,_),house(blue,_,_,_,_),H),
	
	%(9) The man in the center house drinks milk.(put 9 here is for optimization)
	H = [_,_,house(_,_,milk,_,_),_,_],
	
	!,%no need to backtrace.
	
	%(2) The English lives in the red house
	member(house(red,english,_,_,_),H),
	
	%(3) The Swedish keeps dogs.
	member(house(_,swedish,_,_,dog),H),
	
	%(4) The Dane drinks tea.
	member(house(_,dane,tea,_,_),H),
	
	%(5) The green house is just to the left of the white one.
	left(house(green,_,_,_,_),house(white,_,_,_,_),H),
	
	%(6) The owner of the green house drinks coffee.
	member(house(green,_,coffee,_,_),H),
	
	%(7) The Pall Mall smoker keeps birds.
	member(house(_,_,_,pall_mall,bird),H),
	
	%(8) The owner of the yellow house smokes Dunhill.
	member(house(yellow,_,_,dunhill,_),H),
	
	%(11) The Blend smoker has a neighbor who keeps cats.
	neibour(house(_,_,_,blend,_),house(_,_,_,_,cat),H),
	
	%(12) The man who keeps horses lives next to the Dunhill smoker.
	neibour(house(_,_,_,dunhill,_),house(_,_,_,_,horse),H),
	
	%(13) The man who smokes Blue Master drinks beer.
	member(house(_,_,beer,blue_master,_),H),
	 
	%(14) The German smokes Prince.
	member(house(_,german,_,prince,_),H),
	 
	%(16) The Blend smoker has a neighbor who drinks water.
	neibour(house(_,_,_,blend,_),house(_,_,water,_,_),H).

%other rules for reuse.
%X and Y are neibours in H
neibour(X,Y,H):-
	H=[X,Y,_,_,_];H=[_,X,Y,_,_];H=[_,_,X,Y,_];H=[_,_,_,X,Y];
	 %Y < X
	H=[Y,X,_,_,_];H=[_,Y,X,_,_];H=[_,_,Y,X,_];H=[_,_,_,Y,X].
	 
%X if left to in H
left(X,Y,H):- 
	H=[X,Y,_,_,_];H=[_,X,Y,_,_];H=[_,_,X,Y,_];H=[_,_,_,X,Y].

%The question to be answered is: Who keeps fish?
%X keep Y.
keep(X,Y):- houses(H),member(house(_,X,_,_,Y),H).
%get answer
%answer():- keep(X,fish),write(X).

%answer
%the german keep fish.
%H = [
%house(yellow, norwegian, water, dunhill, cat), 
%house(blue, dane, tea, blend, horse),
%shouse(red, english, milk, pall_mall, bird), 
%house(green, german, coffee, prince, _G165586), 
%house(white, swedish, beer, blue_master, dog)
%];