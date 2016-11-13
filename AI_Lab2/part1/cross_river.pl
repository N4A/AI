%Eight people come to the edge of a river, including a policeman, a criminal and a family of six
%people, which consists of a father, a mother, two sons and two daughters. They need to cross the
%river. There is a boat at the river's edge, which can carry two people at a time. Only the policeman,
%the father and the mother can row.
%
%So,define state templete: 
%state(Ship,Police,Criminal,Father,Mother,Son1,Son2,Daughter1,Daughter2)/9

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

%the end rule
path(X,X,[]).%

%the recursion path rule。 important！！！！
%to implement something like BFS by using recursion and backtrace in prolog.
%first, try wheter can get answer via 1 step.
%next, try wheter can get answer via 2 step.
%......
%until find answer via n steps.
%so, it must be the optimal answer.
path(Now,Goal,List):-
	%recurion for answer until get next state 
	path(Now,Next,NewList),
	not(member(Next,NewList)),
	%require that the Next can change to Goal
	%（not the final goal,but Next in previous path-invoking）.  
	move(Next,Goal),
	append(NewList,[Next],List).

%make all eight people arrive safely on the other side of the river.
%You should find the optimal way, i.e. the way with the minimum crossing times.
go(Start,Goal):-
	path(Start,Goal,List),
	length(List,N),writeln(N-'steps.'),
	append(List,[Goal],Answer),write([List,Answer]).
	
%go(state(w,w,w,w,w,w,w,w,w),state(e,e,e,e,e,e,e,e,e)).
%answer:
%17-steps.
%[
%state(w,w,w,w,w,w,w,w,w),state(e,e,e,w,w,w,w,w,w),
%state(w,w,e,w,w,w,w,w,w),state(e,e,e,w,w,e,w,w,w),
%state(w,w,w,w,w,e,w,w,w),state(e,w,w,e,w,e,e,w,w),
%state(w,w,w,w,w,e,e,w,w),state(e,w,w,e,e,e,e,w,w),
%state(w,w,w,e,w,e,e,w,w),state(e,e,e,e,w,e,e,w,w),
%state(w,e,e,w,w,e,e,w,w),state(e,e,e,e,e,e,e,w,w),
%state(w,e,e,e,w,e,e,w,w),state(e,e,e,e,e,e,e,e,w),
%state(w,w,w,e,e,e,e,e,w),state(e,e,w,e,e,e,e,e,e),
%state(w,w,w,e,e,e,e,e,e),state(e,e,e,e,e,e,e,e,e)
%]
