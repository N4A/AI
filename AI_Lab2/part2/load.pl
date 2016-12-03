%load dataset
%tempate: movie(Name,Director,Starring,Year,Area,Type,Score,Num,Comment).
:- ['data/movie.pl'].

area(X,Y):-
	movie(X,_,_,_,Y,_,_,_,_).
	
% search movie via name
search(Name) :-
	movie(Name,Director,Starring,Year,Area,Type,Score,Num,Comment),
	show(movie(Name,Director,Starring,Year,Area,Type,Score,Num,Comment)).

%show the message of the movie, the Name must be correct
show(Name) :- 
	movie(Name,Director,Starring,Year,Area,Type,Score,Num,Comment),
	show(movie(Name,Director,Starring,Year,Area,Type,Score,Num,Comment)).

%show infos of the movie
show(movie(Name,Director,Starring,Year,Area,Type,Score,Num,Comment)):-
	write('电影':Name),nl,
	write('导演':Director),nl,
	write('主演':Starring),nl,
	write('上映时间':Year),nl,
	write('制片国家/地区':Area),nl,
	write('类型':Type),nl,
	write('豆瓣评分':Score),nl,
	write('评分人数':Num),nl,
	write('短评':Comment).