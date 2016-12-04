/*
%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% implement the function that guide the 
% users to choose their movie
% @time 2016/12/4
% @author duocai
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
*/

:- module(guides,[
		guide/0
	]).

guide :- 
	findall(
		movie(Name,Director,Starring,Year,Area,Type,Score),
		movie(Name,Director,Starring,Year,Area,Type,Score,_,_),
		All
		).