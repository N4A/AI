%Eight people come to the edge of a river, including a policeman, a criminal and a family of six
%people, which consists of a father, a mother, two sons and two daughters. They need to cross the
%river. There is a boat at the river's edge, which can carry two people at a time. Only the policeman,
%the father and the mother can row.
%
%So,define state templete: 
%state(Ship,Police,Criminal,Father,Mother,Son1,Son2,Daughter1,Daughter2)/9

%the end rule
path(state(Shi,Pol,Cri,Fat,Mot,Son1,Son2,Dau1,Dau2),
	state(Shi,Pol,Cri,Fat,Mot,Son1,Son2,Dau1,Dau2),List):-
	1=1,
	print(List).

%the recursion path rule
path(state(Shi,Pol,Cri,Fat,Mot,Son1,Son2,Dau1,Dau2),
	state(Shi_g,Pol_g,Cri_g,Fat_g,Mot_g,Son1_g,Son2_g,Dau1_g,Dau2_g),List):-
	% not get end
	(Shi \= Shi_g;Pol \= Pol_g;Cri \= Cri_g,Fat \= Fat_g;
	Mot \= Mot_g;Son1 \= Son1_g;Son2 \= Son2_g;Dau1 \= Dau1_g;Dau2 \= Dau2_g),
	% get next possible state
	move(state(Shi,Pol,Cri,Fat,Mot,Son1,Son2,Dau1,Dau2),
		state(Shi_n,Pol_n,Cri_n,Fat_n,Mot_n,Son1_n,Son2_n,Dau1_n,Dau2_n)),
	% not already in the list
	not(member(state(Shi_n,Pol_n,Cri_n,Fat_n,Mot_n,Son1_n,Son2_n,Dau1_n,Dau2_n),List)),
	% add to the list
	member(state(Shi_n,Pol_n,Cri_n,Fat_n,Mot_n,Son1_n,Son2_n,Dau1_n,Dau2_n),[New|List]),
	% recurion for answer.
	path(state(Shi_n,Pol_n,Cri_n,Fat_n,Mot_n,Son1_n,Son2_n,Dau1_n,Dau2_n),
		state(Shi_g,Pol_g,Cri_g,Fat_g,Mot_g,Son1_g,Son2_g,Dau1_g,Dau2_g),[New|List]).

% the police crosses the river with the criminal.
move(state(X,X,X,Fat,Mot,Son1,Son2,Dau1,Dau2),state(Y,Y,Y,Fat,Mot,Son1,Son2,Dau1,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Y,Y,Fat,Mot,Son1,Son2,Dau1,Dau2))).

% the police crosses the river alone.
move(state(X,X,Cri,Fat,Mot,Son1,Son2,Dau1,Dau2),state(Y,Y,Cri,Fat,Mot,Son1,Son2,Dau1,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Y,Cri,Fat,Mot,Son1,Son2,Dau1,Dau2))).

% the police goes with the father
move(state(X,X,Cri,X,Mot,Son1,Son2,Dau1,Dau2),state(Y,Y,Cri,Y,Mot,Son1,Son2,Dau1,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Y,Cri,Y,Mot,Son1,Son2,Dau1,Dau2))).
	
% the police goes with the mother
move(state(X,X,Cri,Fat,X,Son1,Son2,Dau1,Dau2),state(Y,Y,Cri,Fat,Y,Son1,Son2,Dau1,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Y,Cri,Fat,Y,Son1,Son2,Dau1,Dau2))).
	
% the police goes with the son 1
move(state(X,X,Cri,Fat,Mot,X,Son2,Dau1,Dau2),state(Y,Y,Cri,Fat,Mot,Y,Son2,Dau1,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Y,Cri,Fat,Mot,Y,Son2,Dau1,Dau2))).
	
% the police go with the son 2
move(state(X,X,Cri,Fat,Mot,Son1,X,Dau1,Dau2),state(Y,Y,Cri,Fat,Mot,Son1,Y,Dau1,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Y,Cri,Fat,Mot,Son1,Y,Dau1,Dau2))).

% the police go with the daughter 1
move(state(X,X,Cri,Fat,Mot,Son1,Son2,X,Dau2),state(Y,Y,Cri,Fat,Mot,Son1,Son2,Y,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Y,Cri,Fat,Mot,Son1,Son2,Y,Dau2))).
	
% the police go with the daughter 2
move(state(X,X,Cri,Fat,Mot,Son1,Son2,Dau1,X),state(Y,Y,Cri,Fat,Mot,Son1,Son2,Dau1,Y)):-
	opposite(X,Y),not(unsafe(state(Y,Y,Cri,Fat,Mot,Son1,Son2,Dau1,Y))).
	
% the father go alone 
move(state(X,Pol,Cri,X,Mot,Son1,Son2,Dau1,Dau2),state(Y,Pol,Cri,Y,Mot,Son1,Son2,Dau1,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Pol,Cri,Y,Mot,Son1,Son2,Dau1,Dau2))).
	
% the father goes with the mother
move(state(X,Pol,Cri,X,X,Son1,Son2,Dau1,Dau2),state(Y,Pol,Cri,Y,Y,Son1,Son2,Dau1,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Pol,Cri,Y,Y,Son1,Son2,Dau1,Dau2))).

% the father goes with the son 1
move(state(X,Pol,Cri,X,Mot,X,Son2,Dau1,Dau2),state(Y,Pol,Cri,Y,Mot,Y,Son2,Dau1,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Pol,Cri,Y,Mot,Y,Son2,Dau1,Dau2))).
	
% the father goes with the son 2
move(state(X,Pol,Cri,X,Mot,Son1,X,Dau1,Dau2),state(Y,Pol,Cri,Y,Mot,Son1,Y,Dau1,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Pol,Cri,Y,Mot,Son1,Y,Dau1,Dau2))).
	
% the mother goes alone
move(state(X,Pol,Cri,Fat,X,Son1,Son2,Dau1,Dau2),state(Y,Pol,Cri,Fat,Y,Son1,Son2,Dau1,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Pol,Cri,Fat,Y,Son1,Son2,Dau1,Dau2))).
	
% the mother goes with daughter 1
move(state(X,Pol,Cri,Fat,X,Son1,Son2,X,Dau2),state(Y,Pol,Cri,Fat,Y,Son1,Son2,Y,Dau2)):-
	opposite(X,Y),not(unsafe(state(Y,Pol,Cri,Fat,Y,Son1,Son2,Y,Dau2))).
	
% the mother goes with the daughter 2
move(state(X,Pol,Cri,Fat,X,Son1,Son2,Dau1,X),state(Y,Pol,Cri,Fat,Y,Son1,Son2,Dau1,Y)):-
	opposite(X,Y),not(unsafe(state(Y,Pol,Cri,Fat,Y,Son1,Son2,Dau1,Y))).

% declare 2 opposite sides
opposite(w,e).
opposite(e,w).

%declare the unsafe states
unsafe(state(_,Pol,Cri,Fat,Mot,Son1,Son2,Dau1,Dau2)):-
	%(1) If the policeman is not with the criminal, 
	%the criminal will harm the family of six people.
	(Pol \= Cri,(Cri = Fat;Cri = Mot;Cri = Son1;Cri = Son2;Cri = Dau1;Cri = Dau2));
	%If the mother is not with the father, the father will scold the daughters.
	(Mot \= Fat,(Fat = Dau1;Fat = Dau2));
	%If the father is not with the mother, the mother will scold the sons.
	(Fat \= Mot,(Mot = Son1;Mot = Son2)).

%make all eight people arrive safely on the other side of the river.
%You should find the optimal way, i.e. the way with the minimum crossing times.
go(state(Shi,Pol,Cri,Fat,Mot,Son1,Son2,Dau1,Dau2),
	state(Shi_g,Pol_g,Cri_g,Fat_g,Mot_g,Son1_g,Son2_g,Dau1_g,Dau2_g)):-
	List = [state(Shi,Pol,Cri,Fat,Mot,Son1,Son2,Dau1,Dau2)],
	path(state(Shi,Pol,Cri,Fat,Mot,Son1,Son2,Dau1,Dau2),
		state(Shi_g,Pol_g,Cri_g,Fat_g,Mot_g,Son1_g,Son2_g,Dau1_g,Dau2_g),List).
%go(state(w,w,w,w,w,w,w,w,w),state(e,e,e,e,e,e,e,e,e)).