%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% user choose one movie,
% the procedure try to guess it via some related questions
% @time 2016/12/6
% @author duocai
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


:- module(guesses,[
		guess/0
	]).

%start rule
guess :- 
	findall(
		movie(Name,Director,Starring,Year,Area,Type,Score),
		movie(Name,Director,Starring,Year,Area,Type,Score,_,_),
		All
		),%get all movies
	hypoth_area(Pos),undo,%use area to remove some movies
	area_simply(All,Left,Pos),
	hypoth_time(Time),undo,%use time to remove some movies
	time_simply(Left,Left1,Time),
	(
		check_lens(Left1);%if it is end
		continue(Left1)%if not continue.
	).

% check whether there is only one movie left
check_lens(Left) :-
	length(Left,Len),
	(
		(
			Len = 0,
			write("你的电影不再数据库中.TAT")
		);
		(
			Len = 1,
			nth0(0,Left,Movie),
			movie(Name,_,_,_,_,_,_) = Movie,
			show_answer(Name)
		)
	).

%show answer
show_answer(Name):-
	write("我猜这部电影是:"),write(Name),nl.

%use name to distinguish	
continue(All) :- 
	hypoth_len(NameLen),undo,
	(
		(
			NameLen = 4,
			len4_simply(All,[],Left)
		);
		(
			NameLen = 5,
			more4_simply(All,[],Left)
		);
		(
			NameLen = 3,
			less4_simply(All,[],Left)
		)
	),!,
	(
		check_lens(Left);
		next(Left)
	).

%use the type of movies to distinguish
next(All):- 
	allType(All,[],Types),
	type_simply(All,Types,[],Left),
	(
		length(Left,Len),
		(
			Len > 1,%others, we already get the result
			director(Left)
		);
		1=1%others, we already get the result,and should exit
	).

%use director to distinguish
director(All) :- 
	allDirector(All,[],Dires),
	director_simply(All,Left,Dires),
	(
		length(Left,Len),
		(
			Len > 1,%others, we already get the result
			actor(Left)
		);
		1=1%others, we already get the result,and should exit
	).

%use actor to distinguish	
actor(All) :- 
	allActor(All,[],Actors),
	actor_simply(All,Left,Actors),
	(
		length(Left,Len),
		(
			Len > 1,%others, we already get the result
			write('你也是厉害,是下面电影中的某一部吗？'),nl,
			write(Left)
		);
		1=1%others, we already get the result,and should exit
	).
% end of main proccess
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	


/*
	start of specific rules that use actor to distinguish
*/	
% use actors to distinguish
actor_simply(All,Left,Actors) :- 
	length(Actors,Len),
	(
		Len = 0,%exit if Actors = []
		All = Left
	);
	(
		[H|L] = Actors,
		(
			(
				verify('它是'-H-'主演的吗？'),undo,
				select_Actor(All,H,[],Left),
				(
					check_lens(Left);
					1=1%exit
				)
			);
			(
				delete(All,movie(_,_,H,_,_,_,_),New),
				(
					(
						check_lens(New),
						Left = New%exit
					);
					actor_simply(New,Left,L)%recursion
				)	
			)
		)
	).
	
%select all movies has 'Actor'
%from All list
%resluts are in Left list	
select_Actor(All,Actor,Temp,Left):-
	length(All,Len),
	(
		Len = 0,
		Left = Temp
	);
	(
		[H|L] = All,
		((
			movie(_,_,Actor,_,_,_,_) = H,
			append(Temp,[H],New),
			select_Actor(L,Actor,New,Left)
		);
		select_Actor(L,Actor,Temp,Left)
		)
	).
	
%get all Actors form 'All' list
%result are in 'Actors'	
allActor(All,Temp,Actors):-
	length(All,Len),
	(
		Len = 0,
		Actors = Temp
	);
	(
		[H|L] = All,
		movie(_,_,Actor,_,_,_,_) = H,
		(
			(
				member(Actor,Temp),
				allActor(L,Temp,Actors)
			);
			(
				append(Temp,[Actor],New),
				allActor(L,New,Actors)%recursion
			)
		)
	).
	

/*
	start of specific rules that use director to distinguish
*/	
% use dirdector to dis tinguish
director_simply(All,Left,Dires) :- 
	length(Dires,Len),
	(
		Len = 0,%exit if Dires = []
		All = Left
	);
	(
		[H|L] = Dires,
		(
			(
				verify('它是'-H-'导演的吗？'),undo,
				select_director(All,H,[],Left),
				(
					check_lens(Left);
					1=1%exit
				)
			);
			(
				delete(All,movie(_,H,_,_,_,_,_),New),
				(
					(
						check_lens(New),
						Left = New%exit
					);
					director_simply(New,Left,L)%recursion
				)	
			)
		)
	).
%select all movies directed by Dire
%from All list
%resluts are in Left list	
select_director(All,Dire,Temp,Left):-
	length(All,Len),
	(
		Len = 0,
		Left = Temp
	);
	(
		[H|L] = All,
		((
			movie(_,Dire,_,_,_,_,_) = H,
			append(Temp,[H],New),
			select_director(L,Dire,New,Left)
		);
		select_director(L,Dire,Temp,Left)
		)
	).	
%get all directors form 'All' list
%result are in 'Dires'	
allDirector(All,Temp,Dires):-
	length(All,Len),
	(
		Len = 0,
		Dires = Temp
	);
	(
		[H|L] = All,
		movie(_,Dire,_,_,_,_,_) = H,
		(
			(
				member(Dire,Temp),
				allDirector(L,Temp,Dires)
			);
			(
				append(Temp,[Dire],New),
				allDirector(L,New,Dires)%recursion
			)
		)
	).


/*
	start of specific rules that use types of movies to distinguish
	it's a little complex.
*/
			
%use the type of the movie to distiguish
type_simply(All,Types,Checked,Lefts):-
	nth0(0,Types,Type),
	(
		(
			verify('它是'-Type-'片吗？'),undo,
			select_type(Type,All,[],Left)		
		);
		(
			delete_type(Type,All,[],Left)	
		)
		),
	(
		(
			check_lens(Left),
			Lefts = Left%exit the recursion
		);
		(
			allType(Left,[],Newtypes),
			append(Checked,[Type],New),
			delete_checked(Newtypes,New,Lefttypes),
			
			length(Lefttypes,Len),
			((
				Len > 0,
				type_simply(Left,Lefttypes,New,Lefts)
			);
			(
				Lefts = Left% exit the recursion
			))	
		)
	).
	
delete_checked(Newtypes,Checked,Lefttypes):-
	length(Checked,Len),
	(
		Len = 0,
		Newtypes = Lefttypes
	);
	(
		[H|L] = Checked,
		delete(Newtypes,H,Newleft),
		delete_checked(Newleft,L,Lefttypes)
	).
	
delete_type(Type,All,Temp,Left):-
	length(All,Len),
	(
		Len = 0,
		Left = Temp
	);
	(
		[H|L] = All,
		movie(_,_,_,_,_,Types,_) = H,
		(
			(			
				member(Type,Types),
				delete_type(Type,L,Temp,Left)
				
			);
			(
				append(Temp,[H],New),
				delete_type(Type,L,New,Left)
			)
		)
	).
	
select_type(Type,All,Temp,Left):-
	length(All,Len),
	(
		Len = 0,
		Left = Temp
	);
	(
		[H|L] = All,
		movie(_,_,_,_,_,Types,_) = H,
		(
			(			
				member(Type,Types),
				append(Temp,[H],New),
				select_type(Type,L,New,Left)
				
			);
			(
				select_type(Type,L,Temp,Left)
			)
		)
	).

%get all types of the movies	
allType(All,Temp,Types):-
	length(All,Len),
	(
		Len = 0,
		Types = Temp
	);
	(
		[H|L] = All,
		movie(_,_,_,_,_,Type,_) = H,
		addtype(Type,Temp,New),
		allType(L,New,Types)%recursion
	).

addtype(Type,Temp,Result):-
	length(Type,Len),
	(
		Len = 0,
		Result = Temp
	);
	(
		[H|L] = Type,
		(
			(
				not(member(H,Temp)),
				append(Temp,[H],New),
				addtype(L,New,Result)
			);
			addtype(L,Temp,Result)
		)
	).


/*
	start of specific rules that use the length of name to distinguish
*/
%the length is less than 4
less4_simply(All,Temp,Left) :-
	length(All,Len),
	(
		Len = 0,
		Left = Temp
	);
	(
		[H|L] = All,
		movie(Name,_,_,_,_,_,_) = H,
		atom_length(Name,Namelen),
		(
			(Namelen < 4,append(Temp,[H],New),less4_simply(L,New,Left));
			less4_simply(L,Temp,Left)
		)
	).
%the length is equal to 4
len4_simply(All,Temp,Left) :-
	length(All,Len),
	(
		Len = 0,
		Left = Temp
	);
	(
		[H|L] = All,
		movie(Name,_,_,_,_,_,_) = H,
		atom_length(Name,Namelen),
		(
			(Namelen = 4,append(Temp,[H],New),len4_simply(L,New,Left));
			len4_simply(L,Temp,Left)
		)
	).
%the length is more than 4
more4_simply(All,Temp,Left) :-
	length(All,Len),
	(
		Len = 0,
		Left = Temp
	);
	(
		[H|L] = All,
		movie(Name,_,_,_,_,_,_) = H,
		atom_length(Name,Namelen),
		(
			(Namelen > 4,append(Temp,[H],New),more4_simply(L,New,Left));
			more4_simply(L,Temp,Left)
		)
	).
%ask use for the length of the name
hypoth_len(X):- 
	(
		verify("它的名字是4个字吗？"),
		X = 4
	);
	(
		verify("那是多于4个字吗？"),
		X = 5
	);
	X = 3.


/*
	start of specific rules that use the time when the movie was produced to distinguish
*/

% ask for time
hypoth_time(2010):-
	t2000,
	verify('它是2010年后电影吗?').
hypoth_time(1990):-
	not(yes('它是2000年后电影吗?')),
	verify('它是1990年后电影吗?').
hypoth_time(Time):-
	(
		not(yes('它是2000年后电影吗?')),
		Time = low
	);
	Time = 2000.

t2000:-verify('它是2000年后电影吗?').


time_simply(All,Left,Time):-
	(
		Time = low,
		time_low(All,[],Left)
	);
	time_Specific(All,[],Left,Time).

%time is less than 1990
time_low(All,Temp,Left):-
	length(All,Len),
	(
		Len = 0,
		Left = Temp
	);
	(
		[H|L] = All,
		movie(_,_,_,Year,_,_,_) = H,
		(
			(Year < 1990,append(Temp,[H],New),time_low(L,New,Left));
			time_low(L,Temp,Left)
		)
	).
%time is between 1990 and 2020
time_Specific(All,Temp,Left,Time):-
	length(All,Len),
	(
		Len = 0,
		Left = Temp
	);
	(
		[H|L] = All,
		movie(_,_,_,Year,_,_,_) = H,
		Up is Time+10,
		(
			(
				Year < Up, 
				Year >= Time,
				append(Temp,[H],New),
				time_Specific(L,New,Left,Time)
			);
			time_Specific(L,Temp,Left,Time)
		)
	).
	

/*
	start of specific rules that use area to distinguish
*/
% ask for area
hypoth_area('中国大陆'):-
	asia,
	verify('它是内地电影吗？').
hypoth_area('香港') :-
	asia,
	verify('它是香港电影吗？').
hypoth_area('日本') :- 
	asia,
	verify('它是日本电影吗？').
hypoth_area('台湾') :-
	asia,
	verify('它是台湾电影吗？').
hypoth_area('韩国') :-
	asia,
	verify('它是韩国电影吗？').
hypoth_area('泰国') :-
	asia,
	verify('它是泰国电影吗？').
hypoth_area('印度') :-
	asia,
	verify('它是印度电影吗？').
hypoth_area('美国') :-
	verify('它是美国电影吗？').
hypoth_area('德国') :-
	europe,
	verify('它是德国电影吗？').
hypoth_area('法国') :-
	europe,
	verify('它是法国电影吗？').
hypoth_area('英国') :-
	europe,
	verify('他是英国电影吗？').
hypoth_area('意大利') :-
	europe,
	verify('他是意大利电影吗？').
hypoth_area(others).

asia :-
	verify('它是亚洲电影吗？').
europe :- 
	verify('它是欧洲电影吗？').

% use area info to simplify the list
area_simply(All,Left,Pos):- 
	(
		Pos = others,
		%extract all these movies from the list
		delete_pos(
		['中国大陆','香港','日本','台湾','韩国','美国','德国','法国','英国','意大利','泰国','印度'],
		All,Left
		)
	);
	(
		Pos \= others,
		%select all movie according to locations
		findall(movie(Name,Director,Starring,Year,Pos,Type,Score),
		movie(Name,Director,Starring,Year,Pos,Type,Score,_,_),
		Left)
	).

%use recursion to delete all that in the give locations
delete_pos(Pos,All,Left) :-
	length(Pos,Len),
	(
		Len = 0,
		Left = All
	);
	(
		[H|L] = Pos,
		delete(All,movie(_,_,_,_,H,_,_),Left0),
		delete_pos(L,Left0,Left)
	).	


/*
	some basic rules
*/
% ask questions
ask(Question) :-
    write(Question),
    write("y or n:"),
    read(Response),
    ( (Response == y ; Response == yes)
      ->
       assert(yes(Question)) ;
       assert(no(Question)), fail).

:- dynamic yes/1,no/1.

% verify the question
verify(S) :-
   (
   	yes(S) -> true ;
    (no(S) -> fail; ask(S))%no -> stop and back trace
    ).

% undo all yes/no assertions
undo :- retract(yes(_)),fail. 
undo :- retract(no(_)),fail.
undo.
	