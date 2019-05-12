// vue.config.js
module.exports ={
  "devServer": {
    "port":8088,
    "proxyTable":{
    '/api': {
      "target": 'http://localhost:8088',
      "changeOrigin": true,
      "secure":false,
      ws:true,
      "pathRewrite":{
        '^/api':''
      }
    }
  }// options...
}};

