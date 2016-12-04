%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% implement the function that 
% show the detail infos of the movie decided by the Name of it.
% @time 2016/12/2
% @author duocai
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

:- module(shows,[
		show/1
	]).

%show the message of the movie, the Name must be correct
show(Name) :- 
	movie(Name,Director,Starring,Year,Area,Type,Score,Num,Comment),
	show_brief(Name,Director,Starring,Year,Area,Type,Score,Num,Comment),
	info(Name,Plot),
	nl,show_plot(Plot).

%show infos of the movie
show_brief(Name,Director,Starring,Year,Area,Type,Score,Num,Comment):-
	write('电影':Name),nl,
	write('导演':Director),nl,
	write('主演':Starring),nl,
	write('上映时间':Year),nl,
	write('制片国家/地区':Area),nl,
	write('类型':Type),nl,
	write('豆瓣评分':Score),nl,
	write('评分人数':Num),nl,
	write('短评':Comment).
	
%show the brief plot of the movie
show_plot(Plot) :-
	write('简介':Plot).