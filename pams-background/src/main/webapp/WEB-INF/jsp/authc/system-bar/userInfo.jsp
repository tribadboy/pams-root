<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>  
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" 
    				+ request.getServerPort() + path + "/";
%> 
<!DOCTYPE HTML>
<html>
 <head>
  <title>编辑特定通知用户</title>
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="<%=basePath%>static/view/assets/css/dpl-min.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath%>static/view/assets/css/bui-min.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath%>static/view/assets/css/page-min.css" rel="stylesheet" type="text/css" />
   
 </head>
 <body>
      
  <div class="container">
    <div class="detail-page">   
 		<form id="searchForm" class="form-horizontal" tabindex="0" style="outline: none;">
 		<input type=text style="display:none" name="informId" id="informId" value="${informId }">
          <div class="row">
            <div class="control-group span20">
             	<div class="control-group span8">
            		<label class="control-label">模糊查询：</label>
            		<div class="controls">
              			<input type="text" id="name" name="name" value="" data-rules="{maxlength:30}">
            		</div>
          		</div>      
                <div class="control-group span5">
               		<label>&nbsp;&nbsp;&nbsp;&nbsp;</label>
              		<button id="btnSearch" type="submit" class="button button-primary">搜索</button>
               	</div>
            </div>       
          </div>      
        </form>        
    </div>
    <h1 align="center" style="color:gray;font-size:25px">编辑特定通知的用户</h1>
    <div class="detail-page">
    	<div class="search-grid-container">
      		<div id="grid">
    		</div>
  		</div>
  	</div>
  <script type="text/javascript" src="<%=basePath%>static/view/assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="<%=basePath%>static/view/assets/js/bui-min.js"></script>

  <script type="text/javascript" src="<%=basePath%>static/view/assets/js/config-min.js"></script>
  <script type="text/javascript">
    BUI.use('common/page');
  </script>
  <script type="text/javascript">
  window.onload = function addOption(){
		 //初始化后默认自动点击按钮
		 document.getElementById("btnSearch").click();
	}
  </script>
    <script type="text/javascript">
    var informId = ${informId};
    BUI.use(['bui/form','bui/grid','bui/data'],function(Form,Grid,Data){
    	//创建表单，表单中的日历，不需要单独初始化
        var form = new Form.HForm({
          srcNode : '#searchForm'
        }).render();
    	
         var Grid = BUI.Grid,
          Store = BUI.Data.Store,
          columns = [
            { title: '用户名',width: 100,  sortable: false, dataIndex: 'username'},
            { title: '状态', width: 100, sortable: false, dataIndex: 'status'},
            { title: '是否被通知选定', width: 100, sortable: true, dataIndex: 'flagName'},
            { title: '操作', width: 300, sortable: false, dataIndex: '',renderer:function(value,obj){     
            	if(obj.flagName == "是") {
            		 return '  <span style=\"color:red\" class="grid-command btn-setNo">放弃</span>';
            	} else {
            		 return '  <span class="grid-command btn-setYes">指定</span>';
            	}     
              ;
            }}
          ];

	   var store = new Store({        
            autoLoad:false,
            pageSize:8,
            url : "<%=path%>/web/authc/system/inform/searchInformUserDataInfo",
            params : { 
                nameKey : function() {
                	return encodeURIComponent($("#name").val());
                },
                informId : "#informId"
            },
       }), 
         
          grid = new Grid.Grid({
            render:'#grid',
            loadMask: true,
            forceFit:true,
            columns : columns,
            store: store,
            //plugins : [Grid.Plugins.CheckSelection,Grid.Plugins.AutoFit], //勾选插件、自适应宽度插件
            //plugins : [BUI.Grid.Plugins.CheckSelection], // 插件形式引入多选表格,
            tbar:{             	
            },
            bbar : {
            	pagingBar:true
            }
          });
 
        grid.render();

        form.on('beforesubmit',function(ev) {
          //序列化成对象
          var obj = form.serializeToObject();
          obj.start = 0; //返回第一页
          store.load(obj);
          return false;
        });    
        
        function setYesItem(item){
            if(null != item){
              BUI.Message.Confirm('确认选定该用户么？',function(){
                $.ajax({
                  url : '<%=path%>/web/authc/system/inform/setYesForUser',
                  type: "post",
                  dataType : 'json',
                  data : "informId="+informId+"&targetUserId="+item.userId+"&targetUsername="+item.username,
                  success : function(data){
                    if(data.status == 0){ 
                  	  BUI.Message.Alert('设置成功！');
                  	  store.load();
                    }else{ 
                      BUI.Message.Alert('设置失败！');
                    }
                  }
              });
              },'question');
            }
          }
          
          function setNoItem(item){
              if(null != item){
                BUI.Message.Confirm('确认放弃该用户么？',function(){
                  $.ajax({
                    url : '<%=path%>/web/authc/system/inform/setNoForUser',
                    type: "post",
                    dataType : 'json',
                    data : "informId="+informId+"&targetUserId="+item.userId+"&targetUsername="+item.username,
                    success : function(data){
                      if(data.status == 0){ 
                    	  BUI.Message.Alert('设置成功！');
                    	  store.load();
                      }else{ 
                        BUI.Message.Alert('设置失败！');
                      }
                    }
                });
                },'question');
              }
            }
        
        //监听事件，删除一条记录
        grid.on('cellclick',function(ev){
          var sender = $(ev.domTarget); //点击的Dom
          if(sender.hasClass('btn-setYes')){
              var record = ev.record;
              setYesItem(record);
            } else if(sender.hasClass('btn-setNo')) {
          	  var record = ev.record;
          	  setNoItem(record);
            }
        });
      });
    </script>
   </div>
<body>
</html>  