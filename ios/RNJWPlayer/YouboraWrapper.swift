//
//  YouboraWrapper.swift
//  RNJWPlayer
//
//  Created for Youbora integration
//

import Foundation
import JWPlayerKit
import YouboraJWPlayer4Adapter
import YouboraLib

@objcMembers public class YouboraWrapper: NSObject {

  public var adapter: YBJWPlayerAdapter
  public var adAdapter: YBJWPlayerAdsAdapter
  public var plugin: YBPlugin
  
  public init(weak player: JWPlayer, options: YBOptions) {
    
    self.adapter = YBJWPlayerAdapter(player: player)
    self.adAdapter = YBJWPlayerAdsAdapter(player: player)
    self.plugin = YBPlugin(options: options)
    super.init()
    
  }
  
  public func bindPlugin(){
   
      self.plugin.adapter = self.adapter
      self.plugin.adsAdapter = self.adAdapter
    
  }
  
  public func joinPlugin(plugin: inout YBPlugin){
   
      plugin.adapter = self.adapter
      plugin.adsAdapter = self.adAdapter
    
  }
  
  public func getPlugin() -> YBPlugin {
   
      return self.plugin
    
  }
  
  public func getAdapter() -> YBJWPlayerAdapter {
   
      return self.adapter
    
  }
  
}
