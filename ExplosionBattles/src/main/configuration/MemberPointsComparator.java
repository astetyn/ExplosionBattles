package main.configuration;

import java.util.Comparator;

public class MemberPointsComparator implements Comparator<MemberInLeaderBoard>{
	
	@Override
	public int compare(MemberInLeaderBoard o, MemberInLeaderBoard o2) {
 		return o2.getPoints() - o.getPoints();
	}

}
