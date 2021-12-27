/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.framework;


import java.io.InputStream;
import java.util.ArrayList;

import jp.live2d.ALive2DModel;
import jp.live2d.id.PartsDataID;
import jp.live2d.util.Json;
import jp.live2d.util.Json.Value;
import jp.live2d.util.UtFile;
import jp.live2d.util.UtSystem;


public class L2DPose
{
	private ArrayList< L2DPartsParam[] > partsGroups;
	private long lastTime=0;
	private ALive2DModel lastModel=null;


	public L2DPose()
	{
		partsGroups=new ArrayList<L2DPartsParam[]>();
	}


	
	public void updateParam(ALive2DModel model)
	{
		if ( model == null ) return;

		
		if( ! model.equals(lastModel) )
		{
			
			initParam(model);
		}

		lastModel = model;

		long  curTime = UtSystem.getTimeMSec();
		float deltaTimeSec = ( (lastTime == 0 ) ? 0 : ( curTime - lastTime )/1000.0f);
		lastTime = curTime;

		
		if (deltaTimeSec < 0) deltaTimeSec = 0;

		for (int i = 0; i < partsGroups.size(); i++)
		{
			normalizePartsOpacityGroup(model,partsGroups.get(i),deltaTimeSec);
			copyOpacityOtherParts(model, partsGroups.get(i));
		}
	}


	
	public void initParam(ALive2DModel model)
	{
		if ( model == null ) return;

		for (int i = 0; i < partsGroups.size(); i++)
		{
			L2DPartsParam partsGroup[]=partsGroups.get(i);
			for (int j = 0 ; j < partsGroup.length ; j++ )
			 {
				partsGroup[j].initIndex(model);

				 int partsIndex=partsGroup[j].partsIndex;
				 int paramIndex=partsGroup[j].paramIndex;
				 if(partsIndex<0)continue;

				 boolean v = ( model.getParamFloat( paramIndex ) != 0 ) ;
				 model.setPartsOpacity(partsIndex , (v ? 1.0f : 0.0f) ) ;
				 model.setParamFloat(paramIndex , (v ? 1.0f : 0.0f) ) ;

				 if(partsGroup[j].link==null)continue;
				 for(int k=0;k<partsGroup[j].link.size();k++)
				 {
					 partsGroup[j].link.get(k).initIndex(model);
				 }
			 }
		}
	}


	
	public void normalizePartsOpacityGroup( ALive2DModel model, L2DPartsParam partsGroup[] , float deltaTimeSec )
	{
		 int visibleParts = -1 ;
		 float visibleOpacity = 1.0f ;

		 float CLEAR_TIME_SEC = 0.5f ;
		 float phi = 0.5f ;
		 float maxBackOpacity = 0.15f ;


		 
		 for (int i = 0 ; i <  partsGroup.length; i++ )
		 {
			 int partsIndex=partsGroup[i].partsIndex;
			 int paramIndex=partsGroup[i].paramIndex;

			 if(partsIndex<0)continue;

			 if( model.getParamFloat( paramIndex ) != 0 )
			 {
				 if( visibleParts >= 0 )
				 {
					 break ;
				 }
				 visibleParts = i ;
				 visibleOpacity = model.getPartsOpacity(partsIndex) ;

				 
				 visibleOpacity += deltaTimeSec / CLEAR_TIME_SEC ;
				 if( visibleOpacity > 1 )
				{
					 visibleOpacity = 1 ;
				}
			 }
		 }

		 if( visibleParts < 0 )
		 {
			 visibleParts = 0 ;
			 visibleOpacity = 1 ;
		 }

		 
		 for (int i = 0 ; i <  partsGroup.length ; i++ )
		 {
			 int partsIndex=partsGroup[i].partsIndex;
			 if(partsIndex<0)continue;

			 
			 if( visibleParts == i )
			 {
				 model.setPartsOpacity(partsIndex , visibleOpacity ) ;
			 }
			 
			 else
			 {
				 float opacity = model.getPartsOpacity(partsIndex) ;
				 float a1 ;
				 if( visibleOpacity < phi )
				 {
					 a1 = visibleOpacity*(phi-1)/phi + 1 ; 
				 }
				 else
				 {
					 a1 = (1-visibleOpacity)*phi/(1-phi) ; 
				 }

				 
				 float backOp = (1-a1)*(1-visibleOpacity) ;
				 if( backOp > maxBackOpacity )
				 {
					 a1 = 1 - maxBackOpacity/( 1- visibleOpacity ) ;
				 }

				 if( opacity > a1 )
				{
					 opacity = a1 ;
				}
				 model.setPartsOpacity(partsIndex , opacity ) ;
			 }
		 }
	 }


	
	public void copyOpacityOtherParts(ALive2DModel model, L2DPartsParam partsGroup[])
	{
		for (int i_group = 0; i_group < partsGroup.length; i_group++)
		{
			L2DPartsParam partsParam = partsGroup[i_group];

			if(partsParam.link==null)continue;
			if(partsParam.partsIndex<0)continue;

			float opacity = model.getPartsOpacity( partsParam.partsIndex );

			for (int i_link = 0; i_link < partsParam.link.size(); i_link++)
			{
				L2DPartsParam linkParts = partsParam.link.get(i_link);

				if(linkParts.partsIndex<0)continue;
				model.setPartsOpacity(linkParts.partsIndex, opacity);
			}
		}
	}


	
	public static L2DPose load(InputStream in) throws Exception
	{
		byte[] buf = UtFile.load( in ) ;
		return load(buf);
	}


	
	public static L2DPose load(byte[] buf ) throws Exception
	{
		L2DPose ret = new L2DPose();

		Value json = Json.parseFromBytes( buf ) ;

		
		Value poseListInfo = json.get("parts_visible");
		int poseNum = poseListInfo.getVector(null).size();

		for (int i_pose = 0; i_pose < poseNum; i_pose++) {
			Value poseInfo = poseListInfo.get(i_pose);

			
			Value idListInfo = poseInfo.get("group");
			int idNum = idListInfo.getVector(null).size();
			L2DPartsParam[] partsGroup=new L2DPartsParam[idNum];
			for (int i_group = 0; i_group < idNum; i_group++)
			{
				Value partsInfo=idListInfo.get(i_group);
				L2DPartsParam parts=new L2DPartsParam(partsInfo.get("id").toString());
				partsGroup[i_group] = parts;

				
				if(partsInfo.get("link")==null)continue;
				Value linkListInfo = partsInfo.get("link");
				int linkNum = linkListInfo.getVector(null).size();
				parts.link=new ArrayList<L2DPartsParam>();

				for (int i_link = 0; i_link< linkNum; i_link++)
				{
					L2DPartsParam linkParts=new L2DPartsParam(linkListInfo.get(i_link).toString());
					parts.link.add(linkParts);
				}
			}
			ret.partsGroups.add(partsGroup);
		}
		return ret;
	}
}


class L2DPartsParam
{
	String id;
	int paramIndex=-1;
	int partsIndex=-1;

	ArrayList<L2DPartsParam> link=null;


	public L2DPartsParam(String id)
	{
		this.id=id;
	}


	
	public void initIndex(ALive2DModel model)
	{
		paramIndex=model.getParamIndex("VISIBLE:"+id);

		partsIndex=model.getPartsDataIndex(PartsDataID.getID(id));
		model.setParamFloat(paramIndex, 1);
		//Log.d("live2d",id+ " param:"+paramIndex+" parts:"+partsIndex);
	}
}