%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% implement the function that 
% search the movie according to
% certain infos like type, director, score and so on
% and show the hot list.
% @time 2016/12/2
% @author duocai
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

:- module(searchs,[
		search/1
	]).

%serch via name
search_name(Name,Name):-
	movie(Name,_,_,_,_,_,_,_,_).
%search via area
search_area(Area,Name) :-
	movie(Name,_,_,_,Area,_,_,_,_).
%search via director
search_dire(Dire,Name) :-
	movie(Name,Dire,_,_,_,_,_,_,_).
%via starring	
search_star(Star,Name) :-
	movie(Name,_,Star,_,_,_,_,_,_).
%via type	
search_type(Type,Name) :-
	movie(Name,_,_,_,_,Types,_,_,_),
	member(Type,Types).
%via score	
search_score(Min,Name,Score) :-
	movie(Name,_,_,_,_,_,Score,_,_),
	catch(Min =< Score,error(Err,_Context),
        do_nothing(Err)).

%do nothing but fail
do_nothing(_):- fail.	

%search all movies that have a concern with the key.	
search(Key) :- 
	(
		search_name(Key,Name);
		search_area(Key,Name);
		search_dire(Key,Name);
		search_star(Key,Name);
		search_type(Key,Name)
	),
	write(Name),nl,fail;%use back trace to get all results,
	search_score(Key,Name,Score),write(Name:Score),write('分'),nl,fail;
	1=1.%success